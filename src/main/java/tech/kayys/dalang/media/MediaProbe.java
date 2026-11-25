package tech.kayys.dalang.media;

/**
 * Provider port for read-only media inspection. Implementations may delegate
 * to Manazir, a remote service, or a test fixture; backend types stay private.
 */
public interface MediaProbe {
    String providerId();

    MediaProbeResult probe(MediaProbeRequest request) throws MediaProbeException;
}
