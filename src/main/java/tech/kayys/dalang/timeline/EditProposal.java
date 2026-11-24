package tech.kayys.dalang.timeline;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable, evidence-linked proposal. Creating or reviewing a proposal never
 * changes a timeline; application is a separate explicit operation.
 */
public record EditProposal(
        UUID id,
        UUID projectId,
        UUID baseRevisionId,
        String rationale,
        List<String> evidenceReferences,
        List<ProposedEdit> edits) {

    public EditProposal {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(projectId, "projectId");
        Objects.requireNonNull(baseRevisionId, "baseRevisionId");
        Objects.requireNonNull(rationale, "rationale");
        if (rationale.isBlank()) throw new IllegalArgumentException("rationale must not be blank");
        evidenceReferences = List.copyOf(Objects.requireNonNull(evidenceReferences, "evidenceReferences"));
        edits = List.copyOf(Objects.requireNonNull(edits, "edits"));
        if (edits.isEmpty()) throw new IllegalArgumentException("proposal must contain at least one edit");
        if (evidenceReferences.stream().anyMatch(ref -> ref == null || ref.isBlank())) {
            throw new IllegalArgumentException("evidence references must not be blank");
        }
        if (edits.stream().anyMatch(edit -> !baseRevisionId.equals(edit.operation().baseRevisionId()))) {
            throw new IllegalArgumentException("every proposed operation must target the proposal base revision");
        }
        if (edits.stream().map(ProposedEdit::id).distinct().count() != edits.size()) {
            throw new IllegalArgumentException("duplicate proposed edit id");
        }
    }

    public static EditProposal create(UUID projectId, UUID baseRevisionId, String rationale,
                                      List<String> evidenceReferences, List<EditOperation> operations) {
        return new EditProposal(UUID.randomUUID(), projectId, baseRevisionId, rationale,
                evidenceReferences, operations.stream().map(ProposedEdit::pending).toList());
    }

    public EditProposal approve(UUID editId, String reviewerNote) {
        return updateDecision(editId, reviewerNote, true);
    }

    public EditProposal reject(UUID editId, String reviewerNote) {
        return updateDecision(editId, reviewerNote, false);
    }

    private EditProposal updateDecision(UUID editId, String note, boolean approve) {
        boolean found = false;
        var updated = new java.util.ArrayList<ProposedEdit>(edits.size());
        for (ProposedEdit edit : edits) {
            if (edit.id().equals(editId)) {
                found = true;
                updated.add(approve ? edit.approve(note) : edit.reject(note));
            } else {
                updated.add(edit);
            }
        }
        if (!found) throw new IllegalArgumentException("proposed edit not found: " + editId);
        return new EditProposal(id, projectId, baseRevisionId, rationale, evidenceReferences, updated);
    }

    public List<EditOperation> approvedOperations() {
        return edits.stream().filter(e -> e.decision() == ProposalDecision.APPROVED)
                .map(ProposedEdit::operation).toList();
    }

    public boolean hasPendingEdits() {
        return edits.stream().anyMatch(e -> e.decision() == ProposalDecision.PENDING);
    }
}
