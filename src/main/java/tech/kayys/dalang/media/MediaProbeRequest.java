package tech.kayys.dalang.media;

import java.util.Objects;

/** Request to inspect an immutable source asset without decoding or modifying it. */
public record MediaProbeRequest(MediaAsset asset) {
    public MediaProbeRequest {
        Objects.requireNonNull(asset, "asset");
    }
}
