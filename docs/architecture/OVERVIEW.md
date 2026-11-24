# Dalang architecture overview

## Product shape
Dalang supports two experiences over one domain engine: (1) conversational editing, where a user describes an outcome and receives reviewable proposals/clips; and (2) AI-assisted NLE, where users work in a conventional timeline/preview/inspector and invoke Dalang for targeted analysis or edits. These are presentation modes, not separate engines.

## Runtime forms
The same application services must support embedded Java, standalone runtime/service, native Andalus integration, REST/event APIs, and an MCP adapter. MCP is not the internal architecture. Native in-process calls avoid unnecessary protocol round-trips, schema exposure, and JSON conversion. MCP remains useful for external interoperability.

## Responsibility map
| System | Owns | Does not own |
|---|---|---|
| Dalang | Video domain, analysis orchestration, temporal index, timeline, edit proposals, clipper, render workflow | General agent runtime, codec implementation, generic ML framework |
| Manazir | FFmpeg/media execution wrapper and reusable media operations | Agent planning, video project UX, edit intent |
| Andalus | Generic agent runtime, orchestration, workflows, tool execution, governance, HITL | Video timeline/editing semantics |
| Andalus Framework | Reusable Java contracts and extension SPIs | Dalang domain behavior |
| AQLI | Model loading, inference lifecycle, serving/scheduling | Video project and timeline domain |
| Aljabr | Reusable mathematical, statistical, and ML computation | Video product workflows |

## Logical layers
Experience adapters (chat and NLE); Java SDK/REST/events/MCP; application services (projects, assets, analysis, search, proposals, timeline, render); domain core (identity, temporal types, validation, revisions); media intelligence; Manazir adapter; native Andalus adapter; AQLI/Aljabr integration.

## Core flow
1. Import assets and capture immutable source identity and technical metadata.
2. Run analysis as observable jobs and persist time-aligned observations.
3. Retrieve candidate moments using transcript, scene, visual descriptions, and intent.
4. Generate a versioned edit proposal against a known timeline revision.
5. Validate and present a reviewable diff.
6. Approve selected operations and apply as a new revision.
7. Render through the media provider.
8. Verify output streams, duration, metadata, and provenance.

## Invariants
Source media is immutable. Timeline/EDL is authoritative. Edits reference explicit source and timeline ranges. Proposals identify their base revision. Agent output remains a proposal until approved and applied. Media and inference providers are replaceable.
