package tech.kayys.dalang.timeline;

import java.util.Objects;
import java.util.UUID;
import tech.kayys.dalang.time.MediaTime;

/** Changes a clip's timeline placement without changing its source interval. */
public record MoveClip(UUID baseRevisionId, UUID trackId, UUID clipId, MediaTime timelineStart)
        implements EditOperation {
    public MoveClip {
        Objects.requireNonNull(baseRevisionId, "baseRevisionId");
        Objects.requireNonNull(trackId, "trackId");
        Objects.requireNonNull(clipId, "clipId");
        Objects.requireNonNull(timelineStart, "timelineStart");
        if (timelineStart.ticks() < 0) {
            throw new IllegalArgumentException("timelineStart must not be negative");
        }
    }
}
