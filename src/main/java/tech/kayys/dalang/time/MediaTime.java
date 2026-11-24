package tech.kayys.dalang.time;

import java.math.BigInteger;
import java.util.Objects;

/** Exact media position; ticks are meaningful only with their time base. */
public record MediaTime(long ticks, TimeBase timeBase) implements Comparable<MediaTime> {
    public MediaTime {
        Objects.requireNonNull(timeBase, "timeBase");
    }

    @Override
    public int compareTo(MediaTime other) {
        Objects.requireNonNull(other, "other");
        BigInteger left = BigInteger.valueOf(ticks)
                .multiply(BigInteger.valueOf(timeBase.numerator()))
                .multiply(BigInteger.valueOf(other.timeBase.denominator()));
        BigInteger right = BigInteger.valueOf(other.ticks)
                .multiply(BigInteger.valueOf(other.timeBase.numerator()))
                .multiply(BigInteger.valueOf(timeBase.denominator()));
        return left.compareTo(right);
    }

    /** Convert to a target time base using explicit rounding semantics. */
    public MediaTime rescaleTo(TimeBase target, Rounding rounding) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(rounding, "rounding");
        BigInteger numerator = BigInteger.valueOf(ticks)
                .multiply(BigInteger.valueOf(timeBase.numerator()))
                .multiply(BigInteger.valueOf(target.denominator()));
        BigInteger denominator = BigInteger.valueOf(timeBase.denominator())
                .multiply(BigInteger.valueOf(target.numerator()));
        BigInteger[] qr = numerator.divideAndRemainder(denominator);
        BigInteger value = qr[0];
        if (qr[1].signum() != 0) {
            value = switch (rounding) {
                case EXACT -> throw new ArithmeticException("time is not exactly representable");
                case TOWARD_ZERO -> value;
                case FLOOR -> numerator.signum() < 0 ? value.subtract(BigInteger.ONE) : value;
                case CEILING -> numerator.signum() > 0 ? value.add(BigInteger.ONE) : value;
                case HALF_UP -> qr[1].abs().shiftLeft(1).compareTo(denominator) >= 0
                        ? value.add(BigInteger.valueOf(numerator.signum())) : value;
            };
        }
        return new MediaTime(value.longValueExact(), target);
    }

    public enum Rounding { EXACT, TOWARD_ZERO, FLOOR, CEILING, HALF_UP }
}
