package tech.kayys.dalang.timeline;

import java.util.Objects;
import java.util.UUID;
import tech.kayys.dalang.time.TimeRange;

/** Replaces a clip's selected source interval without changing its timeline start. */
public record TrimClip(UUID baseRevisionId, UUID trackId, UUID clipId, TimeRange sourceRange)
        implements EditOperation {
    public TrimClip {
        Objects.requireNonNull(baseRevisionId, "baseRevisionId");
        Objects.requireNonNull(trackId, "trackId");
        Objects.requireNonNull(clipId, "clipId");
        Objects.requireNonNull(sourceRange, "sourceRange");
    }
}
