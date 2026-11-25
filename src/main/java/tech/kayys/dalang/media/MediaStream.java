package tech.kayys.dalang.media;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import tech.kayys.dalang.time.MediaTime;
import tech.kayys.dalang.time.TimeBase;

/**
 * Technical description of one source stream. Timestamp values retain the
 * stream's own time base; absent metadata remains absent rather than guessed.
 */
public record MediaStream(
        int index,
        String streamId,
        MediaKind kind,
        Optional<String> codecName,
        TimeBase timeBase,
        Optional<MediaTime> startTime,
        Optional<MediaTime> duration,
        OptionalInt width,
        OptionalInt height,
        OptionalInt sampleRate,
        OptionalInt channels) {

    public MediaStream {
        if (index < 0) throw new IllegalArgumentException("index must not be negative");
        streamId = streamId == null ? "" : streamId;
        Objects.requireNonNull(kind, "kind");
        codecName = Objects.requireNonNull(codecName, "codecName");
        Objects.requireNonNull(timeBase, "timeBase");
        startTime = Objects.requireNonNull(startTime, "startTime");
        duration = Objects.requireNonNull(duration, "duration");
        width = Objects.requireNonNull(width, "width");
        height = Objects.requireNonNull(height, "height");
        sampleRate = Objects.requireNonNull(sampleRate, "sampleRate");
        channels = Objects.requireNonNull(channels, "channels");
        codecName.ifPresent(value -> {
            if (value.isBlank()) throw new IllegalArgumentException("codecName must not be blank when present");
        });
        if (duration.isPresent() && duration.get().ticks() < 0) {
            throw new IllegalArgumentException("stream duration must not be negative");
        }
        if (width.isPresent() != height.isPresent()) {
            throw new IllegalArgumentException("width and height must either both be present or both be absent");
        }
        if (width.isPresent() && (width.getAsInt() <= 0 || height.getAsInt() <= 0)) {
            throw new IllegalArgumentException("video dimensions must be positive");
        }
        if (sampleRate.isPresent() && sampleRate.getAsInt() <= 0) {
            throw new IllegalArgumentException("sampleRate must be positive");
        }
        if (channels.isPresent() && channels.getAsInt() <= 0) {
            throw new IllegalArgumentException("channels must be positive");
        }
        if (startTime.isPresent() && !startTime.get().timeBase().equals(timeBase)) {
            throw new IllegalArgumentException("startTime must use the stream time base");
        }
        if (duration.isPresent() && !duration.get().timeBase().equals(timeBase)) {
            throw new IllegalArgumentException("duration must use the stream time base");
        }
    }
}
