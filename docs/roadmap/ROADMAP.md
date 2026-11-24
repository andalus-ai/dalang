# Dalang roadmap

## M0 — Architecture and repository foundation
- [x] Establish product ownership and ecosystem boundaries.
- [x] Define embedded, standalone, native Andalus, REST/events and optional MCP forms.
- [x] Define non-destructive timeline, proposal/approval, provenance and exact-time invariants.
- [x] Document Manazir integration boundary.
- [x] Publish architecture overview, domain model, diagrams and AGENTS.md.
- [ ] Inventory actual public APIs/build conventions in Andalus Framework, Andalus, AQLI, Aljabr and Manazir.
- [ ] Define/implement versioned timeline wire schema and Java domain contracts.
- [ ] Establish Maven structure after source/JDK compatibility review.
- [ ] Add contract tests and standalone example.
Exit: source-validated architecture, reproducible build, tested temporal/timeline contracts, no generated FFM leakage.

## M1 — Media foundation and indexing
Import immutable assets and probe via Manazir; preserve stream IDs/timebases/timestamps/durations; extract timestamped frames/audio; add speech and scene analysis via verified providers; evaluate embedding models by exact identity, task, modality, dimensions, license and AQLI compatibility; use text embeddings for transcript/scene descriptions and verified multimodal models for image/video; implement hybrid lexical/semantic/temporal search and incremental indexing.
Exit: retrieve moments with precise source timestamps and evidence.

## M2 — Timeline, clipper and rendering vertical slice
Implement versioned EDL and validation; insert/trim/split/move/remove/reorder; evidence-backed clip proposals; typed render plan via Manazir; verify output streams/duration/metadata/provenance.
Exit: create, inspect, revise and render non-destructive clips.

## M3 — Agent intelligence and review
Natural-language intent, evidence-backed retrieval, planning, typed proposals, per-operation/partial approval, rejection, revision, undo, job monitoring/cancellation/retry and compact context assembly.
Exit: agent cannot bypass validation, authorization or approval.

## M4 — Runtime, SDK and integrations
Java SDK/embedded runtime; standalone service, REST/OpenAPI and events; native Andalus adapter based on verified contracts; optional MCP and CLI.
Exit: same use cases across entry points.

## M5 — Visual editing
Timeline, preview, asset browser, inspector, captions, render controls, proposal diff/evidence, accept/reject, manual edits, undo/redo and revisions.
Exit: agent and human share one timeline model.

## M6 — Advanced production
Multicam, advanced audio/effects, templates/branding, batch, collaboration and publishing, each with separate acceptance criteria.

## Parallel execution
Source/API inventory, schema design and SDK contract can overlap in M0. M1 ingest and model evaluation overlap after asset/time contracts. Timeline semantics and Manazir media API progress together. Agent planner design can begin early, but end-to-end behavior requires validated edit/render services. UI prototypes may use mocks until schemas stabilize.

## Immediate next actions
1. Complete source API inventory for ecosystem dependencies.
2. Verify Manazir JDK/FFmpeg/jextract matrix and CI.
3. Define versioned timeline schema and temporal conversion rules.
4. Build probe -> timestamped observations -> search -> proposal -> timeline -> render.