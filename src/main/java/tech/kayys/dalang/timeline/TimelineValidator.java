package tech.kayys.dalang.timeline;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/** Deterministic structural validation; media-dependent checks belong to later adapters. */
public final class TimelineValidator {
    public ValidationResult validate(TimelineDocument document, EditProposal proposal) {
        var issues = new ArrayList<ValidationIssue>();
        if (!document.projectId().equals(proposal.projectId())) {
            issues.add(issue("PROPOSAL_PROJECT_MISMATCH", "Proposal belongs to a different project", null, null, null));
        }
        if (!document.revisionId().equals(proposal.baseRevisionId())) {
            issues.add(issue("STALE_PROPOSAL_BASE", "Proposal was authored against a different timeline revision", null, null, null));
        }

        Set<UUID> trackIds = new HashSet<>();
        Set<UUID> clipIds = new HashSet<>();
        document.tracks().forEach(track -> {
            trackIds.add(track.id());
            track.clips().forEach(clip -> clipIds.add(clip.id()));
        });

        for (ProposedEdit edit : proposal.edits()) {
            EditOperation operation = edit.operation();
            if (!trackIds.contains(operation.trackId())) {
                issues.add(issue("TRACK_NOT_FOUND", "Target track does not exist", operation.trackId(), null, edit.id()));
                continue;
            }
            if (operation instanceof InsertClip insert && clipIds.contains(insert.clip().id())) {
                issues.add(issue("CLIP_ALREADY_EXISTS", "Inserted clip ID already exists", operation.trackId(),
                        insert.clip().id(), edit.id()));
            } else if (operation instanceof TrimClip trim && !clipIds.contains(trim.clipId())) {
                issues.add(issue("CLIP_NOT_FOUND", "Trim target clip does not exist", operation.trackId(), trim.clipId(), edit.id()));
            } else if (operation instanceof MoveClip move && !clipIds.contains(move.clipId())) {
                issues.add(issue("CLIP_NOT_FOUND", "Move target clip does not exist", operation.trackId(), move.clipId(), edit.id()));
            } else if (operation instanceof RemoveClip remove && !clipIds.contains(remove.clipId())) {
                issues.add(issue("CLIP_NOT_FOUND", "Remove target clip does not exist", operation.trackId(), remove.clipId(), edit.id()));
            }
        }
        return new ValidationResult(issues);
    }

    private static ValidationIssue issue(String code, String message, UUID trackId, UUID clipId, UUID editId) {
        return new ValidationIssue(code, ValidationSeverity.ERROR, message, trackId, clipId, editId);
    }
}
