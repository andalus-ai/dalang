package tech.kayys.dalang.time;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TimeRangeTest {
    private static final TimeBase TB = new TimeBase(1, 1);

    @Test
    void usesHalfOpenBoundaries() {
        var range = new TimeRange(new MediaTime(2, TB), new MediaTime(5, TB));
        assertTrue(range.contains(new MediaTime(2, TB)));
        assertFalse(range.contains(new MediaTime(5, TB)));
    }

    @Test
    void rejectsEmptyAndReversedRanges() {
        assertThrows(IllegalArgumentException.class,
                () -> new TimeRange(new MediaTime(3, TB), new MediaTime(3, TB)));
        assertThrows(IllegalArgumentException.class,
                () -> new TimeRange(new MediaTime(4, TB), new MediaTime(3, TB)));
    }
}
