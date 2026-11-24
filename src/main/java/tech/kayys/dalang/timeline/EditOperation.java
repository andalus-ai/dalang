package tech.kayys.dalang.timeline;

import java.util.UUID;

/** A typed edit command tied to the exact timeline revision it was authored against. */
public interface EditOperation {
    UUID baseRevisionId();
    UUID trackId();
}
