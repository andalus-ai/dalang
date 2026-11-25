# Media backend integration: Manazir

## Intended relationship
Manazir is the intended FFmpeg/media execution foundation for Dalang. Dalang expresses domain intent (probe asset, extract audio, sample frames, render timeline, create clip); Manazir provides reusable media operations.

**Current source inventory (2026-11-25):** Manazir now contains rational `MediaTimestamp`/`RationalTimeBase` primitives and runtime capability assessment including `MEDIA_PROBE`. The inspected public source does not yet contain a typed probe operation or stream metadata result. Dalang therefore defines a provider port and domain-owned metadata contracts first; an adapter is deferred until Manazir's probe API is stable. Reconcile it with source before implementing an adapter.

## Desired capability groups
### Probe and ingest
ffprobe-backed container/stream metadata; duration/timebase; codec, resolution, frame rate, pixel format, sample rate, channel layout; input validation and structured diagnostics.

### Extraction
Audio extraction with explicit sample/channel policy; frame/keyframe extraction at source timestamps; thumbnails/contact sheets; scene/shot detection integration point; embedded subtitle extraction.

### Editing/rendering
Trim/cut and clip extraction; concat with explicit stream compatibility; multi-track composition from a render plan; scale/crop/pad; audio mix/gain/fades/sync; subtitle/title overlay; codec/quality/container profiles; progress, cancellation, timeout, and process lifecycle.

### Operational contract
Typed request/result objects rather than shell strings; capability/version discovery; structured diagnostics and exit status; safe argument construction (never shell-interpolate user input); bounded concurrency/resources; deterministic temporary-file cleanup; cancellation/timeouts; fixtures and integration tests against supported FFmpeg versions.

## Adapter design
Dalang defines a narrow media-provider port around domain use cases and implements a Manazir adapter against its public API. Do not depend on Manazir internals. If Manazir exposes only low-level wrappers, enhance it with reusable typed operations rather than duplicating wrappers in Dalang.

## Enhancement sequence after source is available
1. Inventory wrapper APIs, execution model, FFmpeg versions, and tests.
2. Stabilize probe/metadata and media-time contracts.
3. Add typed extraction (audio and timestamped frames).
4. Add progress, cancellation, timeout, cleanup, and diagnostics.
5. Add timeline/render-plan composition primitives.
6. Add conformance fixtures for codecs, VFR, audio layouts, and boundary conditions.

Do not assume constant-frame-rate media; preserve rational timebases and handle VFR deliberately.
