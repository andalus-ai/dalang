# Model and media intelligence integration

## Pipeline
1. Probe streams and technical metadata.
2. Detect shots/scenes and sample keyframes.
3. Extract audio and produce timestamped transcription/diarization where available.
4. Generate visual descriptions/observations using a model that supports the required visual modality.
5. Align observations to source-media time.
6. Create text/search representations and embeddings.
7. Persist results with asset IDs, precise time spans, extraction method, model/version, and confidence where meaningful.

## EmbeddingGemma
Do not assume a model called EmbeddingGemma or an informal EmbeddingGemma2 variant accepts raw video or images. The commonly documented EmbeddingGemma family is a text embedding model. Verify the exact model identifier/model card, supported inputs, dimensions, license, and AQLI backend compatibility. A safe initial role is embedding transcript segments, scene descriptions, chapter summaries, and notes. Use a separate vision-language model for visual perception unless the selected model explicitly supports it.

## Model responsibilities
Speech model: timestamped transcript and optional diarization. Vision-language model: frame/clip descriptions, objects/actions, and visual QA. Text embedding model: semantic retrieval over transcripts/descriptions. Optional reranker/LLM: rank moments and translate intent into typed proposals. AQLI owns inference lifecycle/loading/scheduling/serving where compatible; Aljabr provides reusable computation/ML primitives. Neither owns Dalang's timeline domain.

## Temporal index
Index asset summaries, scene/shot intervals, transcript segments, keyframe observations, and optional speaker/object intervals. Every entry preserves asset ID, start/end in source timebase, method, model/version, and underlying observation reference. Avoid a single whole-video embedding when precise clip retrieval is required.

## Retrieval and evaluation
Use hybrid lexical search, metadata filters, semantic retrieval, and optional reranking. Present candidate moments with evidence and exact boundaries. Embedding similarity is not a proxy for virality/editorial quality. Evaluate temporal recall, boundary accuracy, transcript timestamp error, scene quality, duplicate retrieval, latency/resource use by duration/resolution, and reproducibility.
