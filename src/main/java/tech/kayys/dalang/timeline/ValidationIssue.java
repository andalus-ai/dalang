package tech.kayys.dalang.timeline;

import java.util.Objects;
import java.util.UUID;

/** Stable machine-readable diagnostic for timeline or proposal validation. */
public record ValidationIssue(String code, ValidationSeverity severity, String message,
                              UUID trackId, UUID clipId, UUID proposedEditId) {
    public ValidationIssue {
        Objects.requireNonNull(code, "code");
        Objects.requireNonNull(severity, "severity");
        Objects.requireNonNull(message, "message");
        if (code.isBlank() || message.isBlank()) throw new IllegalArgumentException("code and message must not be blank");
    }
}
