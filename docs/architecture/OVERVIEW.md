# Dalang architecture overview

## Product
Dalang is an AI-native video intelligence, editing and production platform. Conversational editing and visual non-linear editing are two experiences over one domain engine. The engine remains usable as a Java library without an LLM or UI.

## Responsibility boundaries
| System | Owns | Does not own |
|---|---|---|
| Dalang | Projects/assets, analysis orchestration, temporal index, edit proposals, timeline/EDL, clipper, render workflows | Generic agent runtime, codec ABI |
| Manazir | Typed media operations, FFmpeg runtime/backends, probe/decode/extract/transcode/mux | Projects, timeline semantics, agent planning |
| Andalus | General agent orchestration, workflows, tools, governance, approval | Video editing domain |
| Andalus Framework | Reusable contracts and SPIs | Dalang concepts |
| AQLI | Model lifecycle, inference and serving | Video project workflows |
| Aljabr | Reusable math/statistics/ML | Video product workflows |

## Layers
1. Experience/transport: chat, NLE, CLI, Java SDK, REST/events, optional MCP.
2. Application: project/assets, ingest, analysis, search, clip selection, proposal/review, timeline, render orchestration.
3. Domain: IDs, projects, assets/streams, observations, tracks/clips, timeline revisions, operations, validation, jobs, artifacts, provenance.
4. Adapters: Manazir media API, AQLI inference, Aljabr compute, Andalus native agent integration, storage/index providers.

No business rules live only in controllers, MCP tools or frontend. Native Andalus integration calls typed application services directly. MCP translates external protocol requests to the same services; it is not an internal call path.

## Workflows
**Understand:** import -> validate -> probe via Manazir -> persist immutable metadata -> extract timestamped observations -> analyze -> index.

**Find/clip:** intent -> hybrid temporal retrieval -> rank candidates -> evidence-backed clip proposal -> review.

**Edit/render:** approved typed operations -> timeline validation -> new immutable revision -> render plan -> Manazir execution -> output verification -> artifact registration.

**Conversation context:** include only relevant project/revision, selected assets, retrieved observations and recent operations; never inject the full corpus into each model request.

## Time and editing semantics
Timeline document is versioned and authoritative. Source assets are immutable. Source stream timestamps and timeline positions are distinct coordinate systems with explicit timebases/frame-rate semantics. Use integer ticks/frames and rational timebases, define rounding, and support VFR.

## Non-functional requirements
Bound resource usage; make jobs cancellable/observable/resumable; enforce workspace isolation and audit; prohibit model-generated shell execution; expose provider capabilities and actionable diagnostics; maintain versioned APIs and representative CFR/VFR/audio/subtitle/malformed-media tests.

See DOMAIN_MODEL.md, MANAZIR_INTEGRATION.md, DIAGRAMS.md and roadmap.