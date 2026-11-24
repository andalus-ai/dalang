package tech.kayys.dalang.timeline;

import java.util.Objects;
import java.util.UUID;
import tech.kayys.dalang.time.MediaTime;
import tech.kayys.dalang.time.TimeRange;

/**
 * A source interval placed at an exact timeline position.
 * Source and timeline coordinates are intentionally kept separate.
 */
public record Clip(
        UUID id,
        UUID assetId,
        int streamIndex,
        TimeRange sourceRange,
        MediaTime timelineStart) {
    public Clip {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(assetId, "assetId");
        Objects.requireNonNull(sourceRange, "sourceRange");
        Objects.requireNonNull(timelineStart, "timelineStart");
        if (streamIndex < 0) {
            throw new IllegalArgumentException("streamIndex must not be negative");
        }
        if (timelineStart.ticks() < 0) {
            throw new IllegalArgumentException("timelineStart must not be negative");
        }
    }

    public static Clip create(UUID assetId, int streamIndex, TimeRange sourceRange, MediaTime timelineStart) {
        return new Clip(UUID.randomUUID(), assetId, streamIndex, sourceRange, timelineStart);
    }
}
