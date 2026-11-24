package tech.kayys.dalang.timeline;

import java.util.Objects;
import java.util.UUID;

/** One typed operation and its independent review decision. */
public record ProposedEdit(UUID id, EditOperation operation, ProposalDecision decision, String reviewerNote) {
    public ProposedEdit {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(operation, "operation");
        Objects.requireNonNull(decision, "decision");
        reviewerNote = reviewerNote == null ? "" : reviewerNote;
    }

    public static ProposedEdit pending(EditOperation operation) {
        return new ProposedEdit(UUID.randomUUID(), operation, ProposalDecision.PENDING, "");
    }

    public ProposedEdit approve(String note) {
        return decide(ProposalDecision.APPROVED, note);
    }

    public ProposedEdit reject(String note) {
        return decide(ProposalDecision.REJECTED, note);
    }

    private ProposedEdit decide(ProposalDecision next, String note) {
        if (decision != ProposalDecision.PENDING) {
            throw new IllegalStateException("edit has already been reviewed: " + id);
        }
        return new ProposedEdit(id, operation, next, note);
    }
}
