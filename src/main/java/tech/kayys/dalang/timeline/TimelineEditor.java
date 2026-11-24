package tech.kayys.dalang.timeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Deterministic in-memory edit application; persistence and authorization are adapter concerns. */
public final class TimelineEditor {
    public TimelineDocument apply(TimelineDocument document, EditOperation operation) {
        Objects.requireNonNull(document, "document");
        Objects.requireNonNull(operation, "operation");
        if (!document.revisionId().equals(operation.baseRevisionId())) {
            throw new StaleTimelineRevisionException(
                    "operation base revision " + operation.baseRevisionId()
                            + " does not match current revision " + document.revisionId());
        }

        List<Track> tracks = new ArrayList<>(document.tracks());
        int trackIndex = indexOfTrack(tracks, operation.trackId());
        Track track = tracks.get(trackIndex);
        List<Clip> clips = new ArrayList<>(track.clips());

        if (operation instanceof InsertClip insert) {
            if (containsClip(document, insert.clip().id())) {
                throw new IllegalArgumentException("clip already exists: " + insert.clip().id());
            }
            clips.add(insert.clip());
        } else if (operation instanceof TrimClip trim) {
            int clipIndex = indexOfClip(clips, trim.clipId());
            Clip old = clips.get(clipIndex);
            clips.set(clipIndex, new Clip(old.id(), old.assetId(), old.streamIndex(),
                    trim.sourceRange(), old.timelineStart()));
        } else if (operation instanceof MoveClip move) {
            int clipIndex = indexOfClip(clips, move.clipId());
            Clip old = clips.get(clipIndex);
            clips.set(clipIndex, new Clip(old.id(), old.assetId(), old.streamIndex(),
                    old.sourceRange(), move.timelineStart()));
        } else if (operation instanceof RemoveClip remove) {
            clips.remove(indexOfClip(clips, remove.clipId()));
        } else {
            throw new IllegalArgumentException("unsupported edit operation: " + operation.getClass().getName());
        }

        tracks.set(trackIndex, new Track(track.id(), track.name(), track.type(), track.order(), clips));
        return document.nextRevision(tracks);
    }

    private static int indexOfTrack(List<Track> tracks, UUID id) {
        for (int i = 0; i < tracks.size(); i++) {
            if (tracks.get(i).id().equals(id)) return i;
        }
        throw new IllegalArgumentException("track not found: " + id);
    }

    private static int indexOfClip(List<Clip> clips, UUID id) {
        for (int i = 0; i < clips.size(); i++) {
            if (clips.get(i).id().equals(id)) return i;
        }
        throw new IllegalArgumentException("clip not found: " + id);
    }

    private static boolean containsClip(TimelineDocument document, UUID id) {
        return document.tracks().stream().flatMap(t -> t.clips().stream())
                .anyMatch(clip -> clip.id().equals(id));
    }
}
