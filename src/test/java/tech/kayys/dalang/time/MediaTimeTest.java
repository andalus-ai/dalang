package tech.kayys.dalang.time;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class MediaTimeTest {
    @Test
    void comparesAcrossDifferentTimeBasesExactly() {
        assertEquals(0, new MediaTime(25, new TimeBase(1, 25))
                .compareTo(new MediaTime(1, new TimeBase(1, 1))));
    }

    @Test
    void rescalesWithExplicitRounding() {
        var source = new MediaTime(1, new TimeBase(1, 3));
        assertEquals(33, source.rescaleTo(new TimeBase(1, 100), MediaTime.Rounding.FLOOR).ticks());
        assertThrows(ArithmeticException.class,
                () -> source.rescaleTo(new TimeBase(1, 100), MediaTime.Rounding.EXACT));
    }

    @Test
    void rejectsInvalidTimeBase() {
        assertThrows(IllegalArgumentException.class, () -> new TimeBase(0, 1));
    }
}
