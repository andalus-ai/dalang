# Dalang domain model and timeline contract

This is the initial conceptual contract; concrete Java types and wire schema require implementation and contract tests.

## Entities
| Entity | Meaning | Invariants |
|---|---|---|
| Workspace | Tenant/security boundary | All access scoped |
| Project | Editing workspace | Settings and current timeline revision |
| Asset | Immutable source/generated media | Stable ID, checksum/provenance, ingest state |
| MediaStream | Video/audio/subtitle stream | Own timebase, start, duration and technical metadata |
| AnalysisJob | Asynchronous analysis | Stages, progress, cancellation and provider provenance |
| Observation | Scene/shot/transcript/speaker/object/action/frame description | Asset and exact source span |
| IndexEntry | Search representation | Observation, asset, span and model version |
| TimelineDocument | Versioned edit decision list | Deterministic, validated, serializable |
| Track | Ordered timeline layer | Stable ID and type |
| Clip | Source range placed on track | Asset/stream, source range and timeline placement |
| EditOperation | Typed modification | Bound to base revision |
| EditProposal | Suggested operations | Evidence, rationale, validation and decisions |
| RenderJob | Render of immutable revision | Revision, profile, backend and execution state |
| Artifact | Derived output | URI, checksum, metadata and provenance |

## Time model
Never use floating-point seconds as canonical time. Use integer ticks plus positive rational timebase. Preserve source stream timebase/timestamps; timeline time is a separate coordinate system. Use half-open intervals [in,out), where in >= 0 and out > in. Define rounding explicitly. Handle VFR, missing/discontinuous timestamps, negative stream starts and packet/frame timestamp distinctions. Nominal frame rate does not reconstruct VFR presentation timestamps.

## Asset and stream metadata
Asset: ID, locator, checksum when available, media type, ingest state, size, container, duration representation and provenance. Stream: index/ID, type, codec, timebase, start timestamp, duration ticks, dimensions/pixel format or sample rate/channel layout, and meaningful frame-rate fields. Do not collapse container duration, stream duration, packet PTS, decoded-frame PTS and timeline duration into one field.

## Timeline document
Versioned document includes schema/document/project/revision IDs, parent revision, project timebase/frame-rate policy and output profile; ordered stable tracks; clips with source asset/stream, source in/out, timeline start/duration and playback rate; transforms/crop/reframe; audio gain/fades/routing; transitions with explicit overlap semantics; captions/titles with temporal bounds; effects/keyframes only when semantics are defined; provenance linking proposals and user operations.

## Operations
Initial deterministic operations: insert/append, trim, move, split, remove, reorder clips/tracks, add/update/remove track, track mute/visibility/lock, caption CRUD, basic audio gain/fade, output profile update. Operations carry target IDs and expected base revision. Reject stale writes or require explicit rebase. Applying creates a new immutable revision and structured diff.

## Deterministic edit application (initial implementation)

The initial Java core exposes typed `InsertClip`, `TrimClip`, `MoveClip`, and `RemoveClip` commands through `TimelineEditor`. Every command carries the exact `baseRevisionId`; stale commands are rejected rather than silently rebased. Successful application returns a new immutable document revision and leaves the prior revision untouched.

The timeline document time base governs clip placement coordinates. Source ranges retain their own source time base. Adapters must explicitly rescale at boundaries and choose a rounding policy; the core does not infer a frame rate or silently convert timestamps. The editor is an in-memory domain service only: persistence, authorization, proposal approval, and rendering remain separate responsibilities.

## Proposal lifecycle
DRAFT -> PROPOSED -> REVIEW -> APPROVED -> APPLIED -> RENDERING -> COMPLETED. Alternate states: REJECTED, CANCELLED, FAILED. Partial approval is tracked per operation. Record project/base revision, operations/affected ranges, evidence references, rationale/confidence, validation, reviewer decisions and resulting revision.

## Validation and provenance
Validate asset/stream existence, source bounds, time conversion, clip duration, placement, track compatibility, overlap/transition semantics, output profile, media availability and backend capabilities. Separate errors from warnings; revalidate before apply and render. Record source checksum, analysis pipeline/model versions, operation history, timeline revision, render profile, Manazir backend/version and output checksum. Do not promise bit-identical renders across different FFmpeg versions or hardware encoders without proof.