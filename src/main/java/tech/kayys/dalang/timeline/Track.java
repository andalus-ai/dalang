package tech.kayys.dalang.timeline;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Immutable ordered lane in a timeline document. */
public record Track(UUID id, String name, TrackType type, int order, List<Clip> clips) {
    public Track {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(type, "type");
        clips = List.copyOf(Objects.requireNonNull(clips, "clips"));
        if (name.isBlank()) throw new IllegalArgumentException("track name must not be blank");
        if (order < 0) throw new IllegalArgumentException("track order must not be negative");
        if (clips.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("clips must not contain null");
        }
    }

    public static Track empty(String name, TrackType type, int order) {
        return new Track(UUID.randomUUID(), name, type, order, List.of());
    }
}
