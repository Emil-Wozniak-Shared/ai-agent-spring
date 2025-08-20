package pl.ejdev.agent.infrastructure.embedding.dto

import org.springframework.ai.embedding.Embedding
import org.springframework.ai.embedding.EmbeddingResponseMetadata

data class GenerateEmbeddingResult(
    val embeddings: List<Embedding>,
    val metadata: EmbeddingResponseMetadata
)