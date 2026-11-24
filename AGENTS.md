# Agent instructions for Dalang

## Mission
Dalang owns video-domain behavior: projects, assets, temporal indexing, edit proposals, timeline semantics, clipper workflows, and render orchestration. Keep Andalus general-purpose.

## Boundaries
- Manazir is the intended FFmpeg/media execution wrapper. Consume its public API and enhance it rather than duplicating media primitives.
- Andalus owns generic agent orchestration, workflows, tools, governance, and HITL.
- Andalus Framework owns reusable contracts and SPIs.
- AQLI owns model inference lifecycle and serving.
- Aljabr owns reusable mathematical, statistical, and ML computation.
- MCP is an interoperability adapter. Prefer typed in-process calls for co-located Java deployments.

## Principles
1. Define contracts before implementation.
2. A versioned, non-destructive timeline/EDL is the source of truth; renders are derived artifacts.
3. Separate propose, review, approve, apply, and render. Never silently overwrite source media.
4. Preserve source IDs, time ranges, rational timebases, provenance, and operation history.
5. Make long-running work observable, cancellable, resumable where practical, and resource-bounded.
6. REST and MCP are adapters over shared application services.
7. Keep modules cohesive; avoid premature fragmentation.
8. Verify model modality and backend compatibility; do not infer them from model names.
9. Inspect dependency source/public APIs before coding against them.
10. Never claim tests/builds passed unless actually run.

## Change workflow
Read architecture and roadmap docs first. Add tests for time arithmetic, timeline validation, serialization compatibility, and lifecycle transitions. Update docs when contracts change.
