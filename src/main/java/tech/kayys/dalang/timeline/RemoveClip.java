package tech.kayys.dalang.timeline;

import java.util.Objects;
import java.util.UUID;

/** Removes a clip from its owning track. */
public record RemoveClip(UUID baseRevisionId, UUID trackId, UUID clipId) implements EditOperation {
    public RemoveClip {
        Objects.requireNonNull(baseRevisionId, "baseRevisionId");
        Objects.requireNonNull(trackId, "trackId");
        Objects.requireNonNull(clipId, "clipId");
    }
}
