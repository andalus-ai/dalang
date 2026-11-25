package tech.kayys.dalang.media;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class MediaAssetTest {
    @Test
    void createsImmutableSourceReference() {
        var asset = MediaAsset.of(URI.create("file:///media/source.mov"), "source.mov");
        assertEquals("file", asset.source().getScheme());
        assertTrue(asset.sha256().isEmpty());
        assertTrue(asset.sizeBytes().isEmpty());
    }

    @Test
    void validatesChecksumSizeAndAbsoluteUri() {
        assertThrows(IllegalArgumentException.class, () ->
                new MediaAsset(UUID.randomUUID(), URI.create("relative.mov"), "clip",
                        Optional.empty(), OptionalLong.empty()));
        assertThrows(IllegalArgumentException.class, () ->
                new MediaAsset(UUID.randomUUID(), URI.create("file:///clip.mov"), "clip",
                        Optional.of("not-a-sha"), OptionalLong.empty()));
        assertThrows(IllegalArgumentException.class, () ->
                new MediaAsset(UUID.randomUUID(), URI.create("file:///clip.mov"), "clip",
                        Optional.empty(), OptionalLong.of(-1)));
    }
}
