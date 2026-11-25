package tech.kayys.dalang.timeline;

import java.util.Objects;

/** Applies only explicitly approved edits after revalidating the proposal against the current revision. */
public final class ProposalApplier {
    private final TimelineValidator validator;
    private final TimelineEditor editor;

    public ProposalApplier() {
        this(new TimelineValidator(), new TimelineEditor());
    }

    public ProposalApplier(TimelineValidator validator, TimelineEditor editor) {
        this.validator = Objects.requireNonNull(validator, "validator");
        this.editor = Objects.requireNonNull(editor, "editor");
    }

    public TimelineDocument applyApproved(TimelineDocument current, EditProposal proposal) {
        Objects.requireNonNull(current, "current");
        Objects.requireNonNull(proposal, "proposal");
        if (proposal.hasPendingEdits()) {
            throw new IllegalStateException("proposal still contains pending edits");
        }
        ValidationResult validation = validator.validate(current, proposal);
        if (!validation.isValid()) {
            throw new IllegalArgumentException("proposal validation failed: " + validation.errors());
        }
        var approved = proposal.approvedOperations();
        if (approved.isEmpty()) {
            throw new IllegalStateException("proposal has no approved edits");
        }
        return editor.applyBatch(current, approved);
    }
}
