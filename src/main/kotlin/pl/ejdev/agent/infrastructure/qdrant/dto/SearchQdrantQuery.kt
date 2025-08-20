package pl.ejdev.agent.infrastructure.qdrant.dto

import org.springframework.ai.vectorstore.SearchRequest.DEFAULT_TOP_K

data class SearchQdrantQuery(
    val queryVector: List<Float> = listOf(),
    val query: String = "",
    val threshold: Double = 0.0,
    val limit: Int = DEFAULT_TOP_K,
)