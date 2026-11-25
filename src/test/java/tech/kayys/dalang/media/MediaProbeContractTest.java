package tech.kayys.dalang.media;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import org.junit.jupiter.api.Test;
import tech.kayys.dalang.time.MediaTime;
import tech.kayys.dalang.time.TimeBase;

class MediaProbeContractTest {
    @Test
    void probePortReturnsContainerAndStreamTimingSeparately() {
        var asset = MediaAsset.of(URI.create("file:///media/vfr.mp4"), "vfr.mp4");
        var timeBase = new TimeBase(1, 90_000);
        var stream = new MediaStream(0, "0", MediaKind.VIDEO, Optional.of("h264"), timeBase,
                Optional.of(new MediaTime(1_800, timeBase)), Optional.of(new MediaTime(900_000, timeBase)),
                OptionalInt.of(1280), OptionalInt.of(720), OptionalInt.empty(), OptionalInt.empty());
        MediaProbe probe = new MediaProbe() {
            @Override public String providerId() { return "fixture"; }
            @Override public MediaProbeResult probe(MediaProbeRequest request) {
                assertEquals(asset.id(), request.asset().id());
                return new MediaProbeResult(asset.id(), providerId(), "mov,mp4,m4a",
                        Optional.of(new MediaTime(0, timeBase)),
                        Optional.of(new MediaTime(900_000, timeBase)), List.of(stream));
            }
        };

        var result = probe.probe(new MediaProbeRequest(asset));

        assertEquals("fixture", result.providerId());
        assertEquals(1_800, result.streams().getFirst().startTime().orElseThrow().ticks());
        assertEquals(900_000, result.containerDuration().orElseThrow().ticks());
    }

    @Test
    void rejectsDuplicateStreamIndexes() {
        var asset = MediaAsset.of(URI.create("file:///media/clip.mp4"), "clip.mp4");
        var tb = new TimeBase(1, 1_000);
        var stream = new MediaStream(0, "", MediaKind.UNKNOWN, Optional.empty(), tb,
                Optional.empty(), Optional.empty(), OptionalInt.empty(), OptionalInt.empty(),
                OptionalInt.empty(), OptionalInt.empty());
        assertThrows(IllegalArgumentException.class, () ->
                new MediaProbeResult(asset.id(), "test", "mp4", Optional.empty(), Optional.empty(),
                        List.of(stream, stream)));
    }

    @Test
    void exposesStructuredProbeFailure() {
        var failure = new MediaProbeException("UNSUPPORTED_INPUT", "fixture", "unsupported");
        assertEquals("UNSUPPORTED_INPUT", failure.code());
        assertEquals("fixture", failure.providerId());
    }
}
