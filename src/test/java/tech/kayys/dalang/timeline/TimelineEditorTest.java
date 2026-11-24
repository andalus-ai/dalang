package tech.kayys.dalang.timeline;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import tech.kayys.dalang.time.MediaTime;
import tech.kayys.dalang.time.TimeBase;
import tech.kayys.dalang.time.TimeRange;

class TimelineEditorTest {
    private static final TimeBase TB = new TimeBase(1, 25);
    private final TimelineEditor editor = new TimelineEditor();

    private TimelineDocument documentWithClip() {
        UUID asset = UUID.randomUUID();
        Clip clip = Clip.create(asset, 0,
                new TimeRange(new MediaTime(0, TB), new MediaTime(100, TB)),
                new MediaTime(0, TB));
        Track track = new Track(UUID.randomUUID(), "V1", TrackType.VIDEO, 0, List.of(clip));
        return new TimelineDocument(1, UUID.randomUUID(), UUID.randomUUID(), null, TB, List.of(track));
    }

    @Test
    void insertsClipAndCreatesNewRevision() {
        var doc = TimelineDocument.empty(UUID.randomUUID(), TB);
        var track = Track.empty("V1", TrackType.VIDEO, 0);
        doc = doc.nextRevision(List.of(track));
        var clip = Clip.create(UUID.randomUUID(), 0,
                new TimeRange(new MediaTime(0, TB), new MediaTime(25, TB)),
                new MediaTime(0, TB));

        var next = editor.apply(doc, new InsertClip(doc.revisionId(), track.id(), clip));
        assertEquals(List.of(clip), next.tracks().getFirst().clips());
        assertEquals(doc.revisionId(), next.parentRevisionId());
    }

    @Test
    void movesClipAndCreatesNewRevision() {
        var doc = documentWithClip();
        var track = doc.tracks().getFirst();
        var clip = track.clips().getFirst();

        var next = editor.apply(doc, new MoveClip(doc.revisionId(), track.id(), clip.id(),
                new MediaTime(50, TB)));

        assertEquals(new MediaTime(50, TB), next.tracks().getFirst().clips().getFirst().timelineStart());
        assertEquals(doc.revisionId(), next.parentRevisionId());
        assertEquals(new MediaTime(0, TB), doc.tracks().getFirst().clips().getFirst().timelineStart());
    }

    @Test
    void trimsSourceRangeWithoutChangingPlacement() {
        var doc = documentWithClip();
        var track = doc.tracks().getFirst();
        var clip = track.clips().getFirst();
        var selected = new TimeRange(new MediaTime(10, TB), new MediaTime(70, TB));

        var next = editor.apply(doc, new TrimClip(doc.revisionId(), track.id(), clip.id(), selected));
        var changed = next.tracks().getFirst().clips().getFirst();

        assertEquals(selected, changed.sourceRange());
        assertEquals(clip.timelineStart(), changed.timelineStart());
    }

    @Test
    void removesClip() {
        var doc = documentWithClip();
        var track = doc.tracks().getFirst();
        var clip = track.clips().getFirst();

        var next = editor.apply(doc, new RemoveClip(doc.revisionId(), track.id(), clip.id()));
        assertTrue(next.tracks().getFirst().clips().isEmpty());
    }

    @Test
    void rejectsStaleOperations() {
        var doc = documentWithClip();
        var track = doc.tracks().getFirst();
        var clip = track.clips().getFirst();

        assertThrows(StaleTimelineRevisionException.class, () ->
                editor.apply(doc, new RemoveClip(UUID.randomUUID(), track.id(), clip.id())));
    }

    @Test
    void rejectsUnknownClip() {
        var doc = documentWithClip();
        var track = doc.tracks().getFirst();
        assertThrows(IllegalArgumentException.class, () ->
                editor.apply(doc, new RemoveClip(doc.revisionId(), track.id(), UUID.randomUUID())));
    }
}
