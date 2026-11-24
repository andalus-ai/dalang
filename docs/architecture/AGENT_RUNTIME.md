# Agent runtime and Andalus integration

## Design rule
Dalang application services and domain types are independent of agent transport. Agents call typed Dalang use cases; they do not construct FFmpeg commands or mutate rendered files directly.

## Agent responsibilities
Understand intent/project context; choose analysis/search strategies; retrieve evidence-backed moments; generate typed edit operations; explain rationale; request approval; monitor jobs. Domain services validate and persist all state.

## Native Andalus path
Create a dedicated adapter after inspecting actual Andalus Framework APIs. Adapt Dalang use cases to framework tool/workflow capabilities, invoke services directly in-process when co-located, and map identity, policy, tracing, cancellation, and approvals. Keep Andalus implementation dependencies at the adapter edge.

## MCP path
Expose a small task-level surface: inspect_project, import_media, analyze_media, search_moments, propose_edit, review_proposal, apply_approved_edit, render_timeline, inspect_job. Keep large transcripts/frame inventories in project storage and retrieve by ID/range rather than injecting them wholesale into model context.

## Standalone path
Use the same application services with durable jobs, storage, REST, and event streams.

## Governance
Treat media and embedded text as untrusted. Require authorization for external/destructive actions and publishing. Enforce workspace/project access in application services. Bound model context, media duration, frame sampling, concurrent jobs, render resources, and output size. Record decisions and evidence references.
