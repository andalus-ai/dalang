package tech.kayys.dalang.time;

/** Exact time base: one tick represents numerator / denominator seconds. */
public record TimeBase(long numerator, long denominator) {
    public TimeBase {
        if (numerator <= 0 || denominator <= 0) {
            throw new IllegalArgumentException("time-base numerator and denominator must be positive");
        }
    }
}
