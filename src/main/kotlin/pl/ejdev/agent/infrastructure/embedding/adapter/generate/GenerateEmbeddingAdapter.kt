package pl.ejdev.agent.infrastructure.embedding.adapter.generate

import org.springframework.ai.embedding.EmbeddingRequest
import org.springframework.ai.openai.OpenAiEmbeddingModel
import org.springframework.ai.openai.OpenAiEmbeddingOptions
import pl.ejdev.agent.infrastructure.embedding.dto.GenerateEmbeddingQuery
import pl.ejdev.agent.infrastructure.embedding.dto.GenerateEmbeddingResult
import pl.ejdev.agent.infrastructure.embedding.port.`in`.GenerateEmbeddingPort

class GenerateEmbeddingAdapter(
    private val openAiEmbeddingModel: OpenAiEmbeddingModel
) : GenerateEmbeddingPort {
    override fun handle(query: GenerateEmbeddingQuery): GenerateEmbeddingResult =
        EmbeddingRequest(listOf(query.text), OpenAiEmbeddingOptions())
            .let(openAiEmbeddingModel::call)
            .let { GenerateEmbeddingResult(it.results, it.metadata) }
}