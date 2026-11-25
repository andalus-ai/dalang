package tech.kayys.dalang.media;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import tech.kayys.dalang.time.MediaTime;

/** Probe snapshot. Container and stream durations are intentionally separate. */
public record MediaProbeResult(
        UUID assetId,
        String providerId,
        String containerFormat,
        Optional<MediaTime> containerStartTime,
        Optional<MediaTime> containerDuration,
        List<MediaStream> streams) {

    public MediaProbeResult {
        Objects.requireNonNull(assetId, "assetId");
        Objects.requireNonNull(providerId, "providerId");
        Objects.requireNonNull(containerFormat, "containerFormat");
        containerStartTime = Objects.requireNonNull(containerStartTime, "containerStartTime");
        containerDuration = Objects.requireNonNull(containerDuration, "containerDuration");
        streams = List.copyOf(Objects.requireNonNull(streams, "streams"));
        if (providerId.isBlank()) throw new IllegalArgumentException("providerId must not be blank");
        if (containerFormat.isBlank()) throw new IllegalArgumentException("containerFormat must not be blank");
        if (containerDuration.isPresent() && containerDuration.get().ticks() < 0) {
            throw new IllegalArgumentException("container duration must not be negative");
        }
        Set<Integer> indexes = new HashSet<>();
        for (MediaStream stream : streams) {
            if (!indexes.add(stream.index())) {
                throw new IllegalArgumentException("duplicate stream index: " + stream.index());
            }
        }
    }
}
