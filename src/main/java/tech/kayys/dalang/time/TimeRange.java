package tech.kayys.dalang.time;

import java.util.Objects;

/** Half-open interval [start, end) in one source or timeline coordinate system. */
public record TimeRange(MediaTime start, MediaTime end) {
    public TimeRange {
        Objects.requireNonNull(start, "start");
        Objects.requireNonNull(end, "end");
        if (start.compareTo(end) >= 0) {
            throw new IllegalArgumentException("range end must be after start");
        }
    }

    public boolean contains(MediaTime point) {
        Objects.requireNonNull(point, "point");
        return start.compareTo(point) <= 0 && point.compareTo(end) < 0;
    }

    public boolean overlaps(TimeRange other) {
        Objects.requireNonNull(other, "other");
        return start.compareTo(other.end) < 0 && other.start.compareTo(end) < 0;
    }
}
