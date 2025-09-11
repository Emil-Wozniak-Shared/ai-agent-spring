package pl.ejdev.agent.infrastructure.qdrant.dto

import org.springframework.ai.embedding.Embedding
import org.springframework.ai.vectorstore.SearchRequest.DEFAULT_TOP_K

data class SearchQdrantEvent(
    val embeddings: List<Embedding>,
    val query: String = "",
    val threshold: Double = 0.0,
    val limit: Int = DEFAULT_TOP_K,
    val keywords: List<Keyword> = listOf()
) {
    data class Keyword(
        val key: String,
        val value: String
    )
}