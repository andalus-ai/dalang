package tech.kayys.dalang.timeline;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import tech.kayys.dalang.time.TimeBase;

/**
 * Immutable, versioned edit decision list. A new edit creates a new revision;
 * this object never mutates in place.
 */
public record TimelineDocument(
        int schemaVersion,
        UUID projectId,
        UUID revisionId,
        UUID parentRevisionId,
        TimeBase timelineTimeBase,
        List<Track> tracks) {

    public static final int CURRENT_SCHEMA_VERSION = 1;

    public TimelineDocument {
        if (schemaVersion != CURRENT_SCHEMA_VERSION) {
            throw new IllegalArgumentException("unsupported timeline schema version: " + schemaVersion);
        }
        Objects.requireNonNull(projectId, "projectId");
        Objects.requireNonNull(revisionId, "revisionId");
        Objects.requireNonNull(timelineTimeBase, "timelineTimeBase");
        tracks = List.copyOf(Objects.requireNonNull(tracks, "tracks"));
        if (tracks.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("tracks must not contain null");
        }
        Set<UUID> trackIds = new HashSet<>();
        Set<UUID> clipIds = new HashSet<>();
        Set<Integer> orders = new HashSet<>();
        for (Track track : tracks) {
            if (!trackIds.add(track.id())) {
                throw new IllegalArgumentException("duplicate track id: " + track.id());
            }
            if (!orders.add(track.order())) {
                throw new IllegalArgumentException("duplicate track order: " + track.order());
            }
            for (Clip clip : track.clips()) {
                if (!clipIds.add(clip.id())) {
                    throw new IllegalArgumentException("duplicate clip id: " + clip.id());
                }
            }
        }
    }

    public static TimelineDocument empty(UUID projectId, TimeBase timelineTimeBase) {
        return new TimelineDocument(CURRENT_SCHEMA_VERSION, projectId, UUID.randomUUID(),
                null, timelineTimeBase, List.of());
    }

    /** Create a new immutable revision while retaining project identity and schema. */
    public TimelineDocument nextRevision(List<Track> nextTracks) {
        return new TimelineDocument(schemaVersion, projectId, UUID.randomUUID(),
                revisionId, timelineTimeBase, nextTracks);
    }
}
