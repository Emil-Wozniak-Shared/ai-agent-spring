package pl.ejdev.agent.infrastructure.qdrant.adapter.search

import org.slf4j.LoggerFactory
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import pl.ejdev.agent.infrastructure.qdrant.dto.SearchQdrantEvent
import pl.ejdev.agent.infrastructure.qdrant.dto.SearchQdrantResult
import pl.ejdev.agent.infrastructure.qdrant.port.out.SearchQdrantPort

class SearchQdrantAdapter(
    private val vectorStore: VectorStore,
) : SearchQdrantPort {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun handle(event: SearchQdrantEvent): SearchQdrantResult {
        return SearchRequest.builder()
            .query(event.query)
            .topK(event.limit)
            .apply {
                val expression = event.keywords.joinToString(" && ") { "${it.key} == '${it.value}'" }
                filterExpression(expression)
            }
            .similarityThreshold(event.threshold)
            .build()
            .also { logger.info("Search Qdrant: $it") }
            .let(vectorStore::similaritySearch)
            .let(::SearchQdrantResult)
    }
}