# Domain model and timeline contract

This is the initial conceptual model. Concrete Java records and wire schemas should follow source/API inventory and repository conventions.

## Entities
- Workspace: tenancy and security boundary.
- Project: editing workspace with output profile and current timeline revision.
- Asset: immutable source/generated media reference with stable ID, locator, checksum, streams, duration, timebase, dimensions, frame rate, audio layout, and ingest state.
- AnalysisJob: asynchronous analysis request with stages, status, progress, cancellation, and provenance.
- MediaObservation: time-bounded scene, shot, transcript, speaker, object/activity, or frame-description result.
- IndexEntry: searchable representation linked to an asset and precise temporal span.
- TimelineDocument: versioned edit decision list.
- EditProposal: candidate changes, rationale, evidence, validation, and base revision.
- RenderJob: execution of an immutable timeline revision.
- Artifact: derived output with checksum, metadata, and provenance.

## Time model
Do not use floating-point seconds as canonical edit time. Use integer ticks/frames with an explicit rational timebase or frame-rate representation. Preserve source and project timebases separately. Source ranges are half-open intervals [in, out), with in >= 0 and out > in. Define conversion/rounding rules and test them. Account for variable-frame-rate media.

## Timeline document
Include schema version, project/revision IDs, project profile, ordered typed tracks, stable clip IDs, source asset references, source in/out, timeline placement/duration, transforms/crop/reframe, audio gain/fades/routing, transitions with explicit overlap semantics, captions/titles/effects with temporal bounds, keyframes where supported, and provenance.

## Initial operations
Insert/append, trim, move, split, remove, reorder, track mute/visibility/lock, add/update caption, basic audio gain/fade, and output profile change. Operations are deterministic, validated against a base revision, and produce a new revision plus structured diff.

## Proposal lifecycle
DRAFT -> PROPOSED -> REVIEW -> APPROVED -> APPLIED -> RENDERING -> COMPLETED. Alternate outcomes include REJECTED, CANCELLED, and FAILED. Model partial approval explicitly. Record base revision, operations, affected ranges, rationale/evidence, validation, decisions, and resulting revision.

## Validation
Check asset existence, source bounds, timebase conversion, clip duration, track compatibility, overlaps/transitions, output profile, and media availability. Distinguish hard errors from warnings and revalidate before render.
