# Dalang architecture diagrams

These describe intended boundaries; concrete adapters must follow verified public APIs.

## Ecosystem context
```mermaid
flowchart TB
 U[User / UI / Client] --> D[Dalang Video Platform]
 A[Andalus Agent Runtime] -->|Native typed adapter| D
 D -->|Typed media API| M[Manazir Media Runtime]
 D -->|Inference adapter| Q[AQLI]
 D -->|Compute adapter| J[Aljabr]
 D --> S[(Project and Media Storage)]
 M --> F[FFmpeg libraries / tools]
```

## Dalang layers
```mermaid
flowchart TB
 subgraph X[Experience and transport]
 C[Chat] --- N[Visual NLE]
 SDK[Java SDK] --- REST[REST / Events]
 MCP[MCP adapter] --- CLI[CLI]
 end
 subgraph APP[Application services]
 P[Projects and assets] --- AN[Analysis]
 SR[Temporal / semantic search] --- ED[Timeline and clips]
 PR[Proposal and review] --- R[Render orchestration]
 end
 subgraph DOM[Domain core]
 T[Versioned timeline] --- O[Typed edit operations]
 V[Validation and revisions] --- EV[Evidence / provenance]
 end
 X --> APP --> DOM
 APP --> MA[Manazir adapter]
 APP --> AA[Andalus adapter]
 APP --> IA[AQLI / Aljabr adapters]
```

## Analysis-to-render workflow
```mermaid
flowchart TD
 I[Import asset] --> P[Probe via Manazir]
 P --> E[Extract timestamped frames/audio]
 E --> A[Scene, transcript and visual analysis]
 A --> IDX[(Temporal and semantic index)]
 Q[User intent] --> RET[Hybrid retrieval]
 IDX --> RET --> R[Rank candidate moments]
 R --> PROP[Evidence-backed edit proposal]
 PROP --> H{Review}
 H -->|Revise| PROP
 H -->|Approve| T[Apply to new timeline revision]
 T --> REN[Render via Manazir]
 REN --> V[Verify output and provenance]
 V --> OUT[(Artifact)]
```

## Proposal lifecycle
```mermaid
stateDiagram-v2
 [*] --> Draft
 Draft --> Proposed
 Proposed --> Review
 Review --> Rejected
 Review --> Draft: Revise
 Review --> Approved: Full or partial approval
 Approved --> Applied: Revalidate base revision
 Applied --> Rendering
 Rendering --> Completed
 Rendering --> Failed
 Proposed --> Cancelled
 Review --> Cancelled
 Failed --> Rendering: Policy-controlled retry
```

## Native versus MCP
```mermaid
flowchart LR
 subgraph Local[Co-located Java process]
 AG[Andalus agent] --> NA[Native Dalang adapter] --> S[Dalang application services]
 end
 subgraph Remote[External process]
 MC[MCP client] --> MS[Dalang MCP server] --> S
 end
 S --> D[Shared domain and application logic]
```
Native calls typed services directly. MCP is an external protocol adapter, never a mandatory internal hop.