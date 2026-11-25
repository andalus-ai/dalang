package tech.kayys.dalang.media;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.OptionalInt;
import org.junit.jupiter.api.Test;
import tech.kayys.dalang.time.MediaTime;
import tech.kayys.dalang.time.TimeBase;

class MediaStreamTest {
    private static final TimeBase TB = new TimeBase(1, 90_000);

    @Test
    void preservesExactStreamTimebaseAndOptionalMetadata() {
        var stream = new MediaStream(2, "v:0", MediaKind.VIDEO, Optional.of("h264"), TB,
                Optional.of(new MediaTime(0, TB)), Optional.of(new MediaTime(900_000, TB)),
                OptionalInt.of(1920), OptionalInt.of(1080), OptionalInt.empty(), OptionalInt.empty());

        assertEquals(TB, stream.timeBase());
        assertEquals(900_000, stream.duration().orElseThrow().ticks());
        assertTrue(stream.sampleRate().isEmpty());
    }

    @Test
    void rejectsTimestampsWithDifferentTimebase() {
        var other = new TimeBase(1, 1_000);
        assertThrows(IllegalArgumentException.class, () ->
                new MediaStream(0, "", MediaKind.AUDIO, Optional.empty(), TB,
                        Optional.of(new MediaTime(0, other)), Optional.empty(),
                        OptionalInt.empty(), OptionalInt.empty(), OptionalInt.of(48_000), OptionalInt.of(2)));
    }

    @Test
    void rejectsIncompleteVideoDimensions() {
        assertThrows(IllegalArgumentException.class, () ->
                new MediaStream(0, "", MediaKind.VIDEO, Optional.empty(), TB,
                        Optional.empty(), Optional.empty(), OptionalInt.of(1920), OptionalInt.empty(),
                        OptionalInt.empty(), OptionalInt.empty()));
    }
}
