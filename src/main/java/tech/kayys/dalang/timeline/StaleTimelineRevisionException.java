package tech.kayys.dalang.timeline;

/** Raised when an edit command was authored against a different timeline revision. */
public final class StaleTimelineRevisionException extends IllegalStateException {
    public StaleTimelineRevisionException(String message) {
        super(message);
    }
}
