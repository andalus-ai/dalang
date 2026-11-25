package tech.kayys.dalang.timeline;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import tech.kayys.dalang.time.MediaTime;
import tech.kayys.dalang.time.TimeBase;
import tech.kayys.dalang.time.TimeRange;

class EditProposalTest {
    private static final TimeBase TB = new TimeBase(1, 25);

    @Test
    void appliesOnlyApprovedEditsAsOneRevision() {
        var projectId = UUID.randomUUID();
        var track = Track.empty("V1", TrackType.VIDEO, 0);
        var initial = TimelineDocument.empty(projectId, TB).nextRevision(List.of(track));
        var clipA = Clip.create(UUID.randomUUID(), 0,
                new TimeRange(new MediaTime(0, TB), new MediaTime(25, TB)), new MediaTime(0, TB));
        var clipB = Clip.create(UUID.randomUUID(), 0,
                new TimeRange(new MediaTime(25, TB), new MediaTime(50, TB)), new MediaTime(25, TB));

        var proposal = EditProposal.create(projectId, initial.revisionId(), "Select two moments",
                List.of("observation://scene/1"), List.of(
                        new InsertClip(initial.revisionId(), track.id(), clipA),
                        new InsertClip(initial.revisionId(), track.id(), clipB)));
        var approved = proposal.approve(proposal.edits().getFirst().id(), "Keep first moment")
                .reject(proposal.edits().getLast().id(), "Duplicate content");

        var result = new ProposalApplier().applyApproved(initial, approved);

        assertEquals(List.of(clipA), result.tracks().getFirst().clips());
        assertEquals(initial.revisionId(), result.parentRevisionId());
        assertTrue(initial.tracks().getFirst().clips().isEmpty());
    }

    @Test
    void refusesToApplyWhileAnyEditIsPending() {
        var projectId = UUID.randomUUID();
        var track = Track.empty("V1", TrackType.VIDEO, 0);
        var initial = TimelineDocument.empty(projectId, TB).nextRevision(List.of(track));
        var clip = Clip.create(UUID.randomUUID(), 0,
                new TimeRange(new MediaTime(0, TB), new MediaTime(25, TB)), new MediaTime(0, TB));
        var proposal = EditProposal.create(projectId, initial.revisionId(), "Add clip", List.of(),
                List.of(new InsertClip(initial.revisionId(), track.id(), clip)));

        assertThrows(IllegalStateException.class,
                () -> new ProposalApplier().applyApproved(initial, proposal));
    }

    @Test
    void rejectsStaleProposalDuringValidation() {
        var projectId = UUID.randomUUID();
        var track = Track.empty("V1", TrackType.VIDEO, 0);
        var initial = TimelineDocument.empty(projectId, TB).nextRevision(List.of(track));
        var proposal = EditProposal.create(projectId, initial.revisionId(), "Remove later",
                List.of(), List.of(new RemoveClip(initial.revisionId(), track.id(), UUID.randomUUID())));
        var changed = initial.nextRevision(initial.tracks());
        var result = new TimelineValidator().validate(changed, proposal);

        assertTrue(result.errors().stream().anyMatch(i -> i.code().equals("STALE_PROPOSAL_BASE")));
    }

    @Test
    void rejectsDuplicateDecisionAndUnknownEdit() {
        var projectId = UUID.randomUUID();
        var track = Track.empty("V1", TrackType.VIDEO, 0);
        var initial = TimelineDocument.empty(projectId, TB).nextRevision(List.of(track));
        var clip = Clip.create(UUID.randomUUID(), 0,
                new TimeRange(new MediaTime(0, TB), new MediaTime(25, TB)), new MediaTime(0, TB));
        var proposal = EditProposal.create(projectId, initial.revisionId(), "Add clip", List.of(),
                List.of(new InsertClip(initial.revisionId(), track.id(), clip)));
        var editId = proposal.edits().getFirst().id();
        var reviewed = proposal.approve(editId, "Looks good");

        assertThrows(IllegalStateException.class, () -> reviewed.reject(editId, "Changed mind"));
        assertThrows(IllegalArgumentException.class, () -> proposal.approve(UUID.randomUUID(), "Unknown"));
    }
}
