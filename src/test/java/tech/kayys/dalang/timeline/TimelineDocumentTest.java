package tech.kayys.dalang.timeline;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import tech.kayys.dalang.time.MediaTime;
import tech.kayys.dalang.time.TimeBase;
import tech.kayys.dalang.time.TimeRange;

class TimelineDocumentTest {
    private static final TimeBase TB = new TimeBase(1, 25);

    @Test
    void createsImmutableRevisions() {
        UUID projectId = UUID.randomUUID();
        var first = TimelineDocument.empty(projectId, TB);
        var track = Track.empty("V1", TrackType.VIDEO, 0);
        var second = first.nextRevision(List.of(track));

        assertEquals(projectId, second.projectId());
        assertEquals(first.revisionId(), second.parentRevisionId());
        assertNotEquals(first.revisionId(), second.revisionId());
        assertTrue(first.tracks().isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> second.tracks().add(track));
    }

    @Test
    void rejectsDuplicateTrackOrder() {
        var first = Track.empty("V1", TrackType.VIDEO, 0);
        var second = Track.empty("V2", TrackType.VIDEO, 0);
        assertThrows(IllegalArgumentException.class, () ->
                new TimelineDocument(1, UUID.randomUUID(), UUID.randomUUID(), null, TB,
                        List.of(first, second)));
    }

    @Test
    void rejectsDuplicateClipIdentityAcrossTracks() {
        UUID asset = UUID.randomUUID();
        var range = new TimeRange(new MediaTime(0, TB), new MediaTime(25, TB));
        var clip = Clip.create(asset, 0, range, new MediaTime(0, TB));
        var first = new Track(UUID.randomUUID(), "V1", TrackType.VIDEO, 0, List.of(clip));
        var second = new Track(UUID.randomUUID(), "V2", TrackType.VIDEO, 1, List.of(clip));
        assertThrows(IllegalArgumentException.class, () ->
                new TimelineDocument(1, UUID.randomUUID(), UUID.randomUUID(), null, TB,
                        List.of(first, second)));
    }
}
