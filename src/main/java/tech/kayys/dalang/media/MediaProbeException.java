package tech.kayys.dalang.media;

import java.util.Objects;

/** Structured provider failure; callers should not parse backend stderr or messages. */
public final class MediaProbeException extends RuntimeException {
    private final String code;
    private final String providerId;

    public MediaProbeException(String code, String providerId, String message) {
        super(message);
        this.code = requireText(code, "code");
        this.providerId = requireText(providerId, "providerId");
    }

    public MediaProbeException(String code, String providerId, String message, Throwable cause) {
        super(message, cause);
        this.code = requireText(code, "code");
        this.providerId = requireText(providerId, "providerId");
    }

    public String code() {
        return code;
    }

    public String providerId() {
        return providerId;
    }

    private static String requireText(String value, String name) {
        Objects.requireNonNull(value, name);
        if (value.isBlank()) throw new IllegalArgumentException(name + " must not be blank");
        return value;
    }
}
