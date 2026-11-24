# Manazir integration contract

## Ownership

Dalang owns video-product concepts: workspaces, projects, immutable assets, analysis observations, temporal indexes, edit proposals, timelines, clipper workflows, and render-job intent.

Manazir owns media execution: probing, stream metadata, timestamp-preserving extraction, transcoding, filtering, muxing, and media-runtime diagnostics. Manazir must not depend on Dalang.

## Dependency direction

```text
Dalang domain and application services
          |
          v
Dalang Manazir adapter
          |
          v
Manazir public media API
          |
          v
Manazir backend SPI
          |
          +-- native FFM backend (future)
          +-- process backend (optional)
          |
          v
generated FFmpeg FFM bindings
```

Dalang must not import `tech.kayys.manazir.avutil` or expose `MemorySegment`, native pointers, FFmpeg structs, or FFmpeg-specific enums in its public API.

## Initial operations required by Dalang

1. Probe a media source and return container and stream metadata.
2. Extract selected video frames with source timestamps and time bases preserved.
3. Extract audio segments with source timing preserved.
4. Transcode or render an explicitly described operation plan.
5. Report progress, cancellation, diagnostics, and output artifacts.

These operations should be added to Manazir incrementally. Dalang must capability-check optional operations rather than assuming all backends support them.

## Time contract

Manazir is the owner of the shared low-level media timestamp primitives: integer ticks plus a positive rational time base. Dalang timeline positions remain distinct from source timestamps and must carry their own time base. Conversions must be explicit and define rounding behavior. Floating-point seconds are presentation values only.

## Resource and safety contract

- Media operations accept typed options, not arbitrary shell command strings.
- Process backends use argument vectors and never interpolate user input into a shell.
- Every operation has bounded resource options and structured diagnostics.
- Long-running operations expose cancellation and progress.
- Native handles and FFM memory segments never cross the Manazir public API.
- Source assets are read-only; derived artifacts have explicit destinations and provenance.

## Compatibility strategy

Dalang should compile against a released Manazir API version. Until Manazir publishes a stable artifact, keep the adapter isolated and avoid introducing an unverified dependency on generated binding classes. Contract tests should validate timestamp preservation and metadata mapping once the first probe/extraction vertical slice exists.
