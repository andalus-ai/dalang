package tech.kayys.dalang.media;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.UUID;

/** Immutable reference to source media; registering an asset never modifies its bytes. */
public record MediaAsset(
        UUID id,
        URI source,
        String displayName,
        Optional<String> sha256,
        OptionalLong sizeBytes) {

    public MediaAsset {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(displayName, "displayName");
        sha256 = Objects.requireNonNull(sha256, "sha256");
        sizeBytes = Objects.requireNonNull(sizeBytes, "sizeBytes");
        if (!source.isAbsolute()) throw new IllegalArgumentException("source URI must be absolute");
        if (displayName.isBlank()) throw new IllegalArgumentException("displayName must not be blank");
        sha256.ifPresent(value -> {
            if (!value.matches("(?i)[0-9a-f]{64}")) {
                throw new IllegalArgumentException("sha256 must contain exactly 64 hexadecimal characters");
            }
        });
        if (sizeBytes.isPresent() && sizeBytes.getAsLong() < 0) {
            throw new IllegalArgumentException("sizeBytes must not be negative");
        }
    }

    public static MediaAsset of(URI source, String displayName) {
        return new MediaAsset(UUID.randomUUID(), source, displayName, Optional.empty(), OptionalLong.empty());
    }
}
