# Dalang

Dalang is the video-editing agent platform in the Andalus ecosystem.

It is designed to support both conversational video editing and AI-assisted non-linear editing (NLE) experiences. The same domain runtime and versioned timeline model power standalone use, native Andalus integration, and optional MCP interoperability.

## Architecture direction

- **Dalang owns video-domain behavior:** media understanding, project and asset models, indexing, edit planning, timeline semantics, clipper workflows, and render orchestration.
- **Manazir owns media execution:** the existing FFmpeg wrapper is the intended media-processing foundation. Dalang should consume stable Manazir APIs and contribute enhancements there rather than creating a competing FFmpeg abstraction.
- **Andalus remains general-purpose:** Dalang integrates through typed in-process adapters where possible; MCP is an interoperability adapter, not an internal call path.
- **AQLI owns inference lifecycle and serving.**
- **Aljabr owns reusable mathematical, statistical, and ML computation.**

## Repository status

This repository is being bootstrapped. The initial milestone establishes architecture, domain contracts, integration boundaries, and an incremental roadmap before implementation modules are introduced.

See [AGENTS.md](AGENTS.md), [architecture documentation](docs/architecture/OVERVIEW.md), and the [roadmap](docs/roadmap/ROADMAP.md).

## Status

Pre-implementation foundation. API and module names in the architecture documents are design proposals until validated against the current Andalus, AQLI, Aljabr, and Manazir source code.
