package tech.kayys.dalang.timeline;

import java.util.Objects;
import java.util.UUID;

/** Inserts a new clip into a track. */
public record InsertClip(UUID baseRevisionId, UUID trackId, Clip clip) implements EditOperation {
    public InsertClip {
        Objects.requireNonNull(baseRevisionId, "baseRevisionId");
        Objects.requireNonNull(trackId, "trackId");
        Objects.requireNonNull(clip, "clip");
    }
}
