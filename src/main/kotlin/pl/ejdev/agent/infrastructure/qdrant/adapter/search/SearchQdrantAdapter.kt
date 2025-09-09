package pl.ejdev.agent.infrastructure.qdrant.adapter.search

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import pl.ejdev.agent.infrastructure.qdrant.dto.SearchQdrantEvent
import pl.ejdev.agent.infrastructure.qdrant.dto.SearchQdrantResult
import pl.ejdev.agent.infrastructure.qdrant.port.out.SearchQdrantPort

class SearchQdrantAdapter(
    private val vectorStore: VectorStore,
) : SearchQdrantPort {
    private val logger = KotlinLogging.logger {}

    override fun handle(event: SearchQdrantEvent): SearchQdrantResult =
        SearchRequest.builder()
            .query(event.query)
            .topK(event.limit)
            .withExpression(event)
            .similarityThreshold(event.threshold)
            .build()
            .also { logger.info { "Search Qdrant: $it" } }
            .let(vectorStore::similaritySearch)
            .let(::SearchQdrantResult)

    private fun SearchRequest.Builder.withExpression(event: SearchQdrantEvent) = apply {
        val expression = event.keywords.joinToString(" && ") { "${it.key} == '${it.value}'" }
        filterExpression(expression)
    }
}