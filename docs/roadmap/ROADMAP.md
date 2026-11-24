# Dalang roadmap

## M0 — Architecture and repository foundation (current)
Establish ownership, architecture, domain model, timeline principles, runtime integration, model strategy, SDK direction, and roadmap. Inventory actual Andalus Framework, Andalus, AQLI, Aljabr, and Manazir public APIs before choosing dependencies. Deliver reviewed architecture and a buildable module-skeleton proposal. Exit when ownership is clear and adapter seams are validated against source.

## M1 — Media foundation and indexing
Integrate Manazir probe/metadata and extraction; import assets and preserve timebases; extract keyframes/audio; create timestamped observations; integrate supported speech/vision models through AQLI where feasible; embed transcript/scene-description text; implement hybrid temporal search. Exit: analyze an asset and retrieve moments with precise timestamps/provenance.

## M2 — Timeline, clipper, and render engine
Implement versioned timeline/EDL and validation; core operations (insert, trim, split, move, remove, reorder); clip proposals from temporal spans; render through Manazir; verify output metadata/duration/streams and provenance. Exit: create, inspect, revise, and render a non-destructive clip project.

## M3 — Agent intelligence and review
Natural-language intent, evidence-backed moment selection, multi-step plans, typed proposals, per-operation approval/partial acceptance/rejection/revision/undo, job monitoring and recovery. Exit: safe proposals never bypass domain validation or approval.

## M4 — Runtime, SDK, integrations
Java SDK/embedded runtime, standalone service, REST/OpenAPI/events, native Andalus integration using typed in-process services, optional MCP adapter, and CLI if useful. Exit: same use cases across runtime entry points.

## M5 — Visual editing experience
Timeline, preview, asset browser, inspector, captions, render controls, proposal diff, accept/reject, manual edits, undo/redo, revision history. Exit: agent and human edits share one model.

## M6 — Advanced production
Multicam, advanced audio, motion/effects, templates/branding, batch, collaboration, publishing. Define exit criteria per capability.

## Parallel work
M0 API inventory and SDK contract design can overlap. M1 ingest/extraction and model evaluation can overlap after asset/timebase contracts are agreed. M2 timeline semantics precede a robust editing agent and visual timeline UI. Manazir enhancements can proceed alongside Dalang after its source/API inventory.

## Immediate next actions
1. Make Manazir source available and inspect the existing wrapper.
2. Inspect public APIs/build conventions in Andalus Framework, Andalus, AQLI, and Aljabr.
3. Convert the design into a minimal Java module skeleton aligned with repository conventions.
4. Implement the first vertical slice after provider and timeline contracts are validated.
