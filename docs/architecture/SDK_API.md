# SDK and API direction

## Principles
Java SDK is the first-class developer interface. REST/OpenAPI and event schemas enable future clients. All transports call the same application services. Use stable IDs and explicit workspace/project scope. Long-running work is represented by durable jobs/events. Editing APIs are revision-aware and non-destructive.

## Initial resources
Workspace, Project, Asset, AnalysisJob, Observation/IndexEntry, AgentSession, EditProposal, TimelineRevision, RenderJob, Artifact.

## Illustrative Java usage
```java
try (DalangRuntime runtime = DalangRuntime.builder()
        .mediaProvider(manazirProvider)
        .build()) {
    Project project = runtime.projects().create(ProjectRequest.named("Interview"));
    Asset asset = runtime.assets().importMedia(project.id(), sourcePath);
    AnalysisJob analysis = runtime.analysis().analyze(
        project.id(), AnalysisRequest.standard(asset.id()));
    SearchResults moments = runtime.search().moments(
        project.id(), MomentQuery.text("audience laughing"));
    EditProposal proposal = runtime.agent().proposeEdit(
        project.id(), EditIntent.createShortHighlights(moments));
    ProposalReview review = runtime.proposals().review(proposal.id());
    TimelineRevision revision = runtime.proposals().apply(
        proposal.id(), review.approval());
    RenderJob render = runtime.render().render(
        project.id(), revision.id(), RenderRequest.platformPreset("vertical-social"));
}
```
This is a proposed shape, not a committed API signature.

## Transport surface
Java embedded SDK; HTTP resources for projects/assets/analysis/search/proposals/timeline/render; SSE initially for events (WebSocket only if justified); concise MCP task adapter.

## Event envelope
Include event ID/type/schema version/timestamp, workspace/project scope, correlation ID, job/proposal/revision ID, and typed payload. Support replay/cursors where storage allows.

## Compatibility
Version public schemas. Timeline documents and events need explicit schema versions and migration policy. Do not expose FFmpeg command lines or provider-specific model request structures as stable API.
