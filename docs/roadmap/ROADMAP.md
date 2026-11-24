# Dalang roadmap

## M0 — Architecture and repository foundation (in progress)

- [x] Establish product ownership: Dalang owns video-domain behavior; Manazir owns media execution; Andalus remains general-purpose.
- [x] Define standalone, embedded, native Andalus, REST/event, and optional MCP runtime forms.
- [x] Define non-destructive timeline, proposal/approval, provenance, and exact-time invariants.
- [x] Document the Manazir integration boundary and required media operations.
- [ ] Inventory public APIs and build conventions in Andalus Framework, Andalus, AQLI, and Aljabr.
- [ ] Define the versioned timeline wire schema and first Java domain contracts.
- [ ] Establish the Dalang Maven module skeleton after dependency and JDK compatibility are verified.
- [ ] Add contract tests and a minimal standalone example.

**Exit criteria:** ownership and adapter seams are validated against source; a clean checkout has a reproducible build; core timeline contracts have tests; no generated FFmpeg ABI types leak into Dalang.

## M1 — Media foundation and indexing

- Integrate Manazir probe/metadata and timestamp-preserving extraction.
- Import immutable assets and preserve source stream time bases.
- Extract keyframes/audio and create timestamped observations.
- Integrate speech/vision models through verified AQLI capabilities where feasible.
- Embed transcript and scene-description text using a replaceable provider; verify EmbeddingGemma model identity and modality before implementation.
- Implement hybrid temporal search with source provenance.

**Exit:** analyze an asset and retrieve moments with precise timestamps and provenance.

## M2 — Timeline, clipper, and render engine

- Implement versioned timeline/EDL and validation.
- Implement insert, trim, split, move, remove, and reorder.
- Generate clip proposals from temporal spans.
- Render through Manazir's typed media API.
- Verify output metadata, duration, streams, and provenance.

**Exit:** create, inspect, revise, and render a non-destructive clip project.

## M3 — Agent intelligence and review

- Natural-language intent and evidence-backed moment selection.
- Multi-step plans and typed proposals.
- Per-operation approval, partial acceptance, rejection, revision, and undo.
- Job monitoring and recovery.

**Exit:** safe proposals cannot bypass domain validation or approval.

## M4 — Runtime, SDK, and integrations

- Java SDK and embedded runtime.
- Standalone service, REST/OpenAPI, and events.
- Native Andalus integration using typed in-process services.
- Optional MCP adapter and CLI.

**Exit:** same use cases across runtime entry points.

## M5 — Visual editing experience

- Timeline, preview, asset browser, inspector, captions, and render controls.
- Proposal diff, accept/reject, manual edits, undo/redo, and revision history.

**Exit:** agent and human edits share one model.

## M6 — Advanced production

- Multicam, advanced audio, motion/effects, templates/branding, batch, collaboration, and publishing.
- Define independent exit criteria per capability.

## Parallel work

M0 API inventory and SDK contract design can overlap. M1 ingest/extraction and model evaluation can overlap after asset/timebase contracts are agreed. Timeline semantics precede a robust editing agent and visual timeline UI. Manazir API work proceeds in parallel, but Dalang consumes only its stable public API.

## Immediate next actions

1. Complete public API inventory for Andalus Framework, Andalus, AQLI, and Aljabr.
2. Confirm Manazir JDK/FFmpeg/jextract toolchain and CI matrix.
3. Define Dalang timeline schema against Manazir exact timestamp primitives.
4. Build the first end-to-end slice: probe -> timestamped observations -> clip proposal -> timeline -> render.
