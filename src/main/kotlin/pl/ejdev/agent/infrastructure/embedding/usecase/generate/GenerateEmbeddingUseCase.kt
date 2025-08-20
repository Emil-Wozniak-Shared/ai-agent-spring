package pl.ejdev.agent.infrastructure.embedding.usecase.generate

import pl.ejdev.agent.infrastructure.base.usecase.UseCase
import pl.ejdev.agent.infrastructure.embedding.dto.GenerateEmbeddingEvent
import pl.ejdev.agent.infrastructure.embedding.dto.GenerateEmbeddingQuery
import pl.ejdev.agent.infrastructure.embedding.dto.GenerateEmbeddingResult
import pl.ejdev.agent.infrastructure.embedding.port.`in`.GenerateEmbeddingPort

class GenerateEmbeddingUseCase(
    private val generateEmbeddingPort: GenerateEmbeddingPort
) : UseCase<GenerateEmbeddingEvent, GenerateEmbeddingResult> {
    override fun handle(event: GenerateEmbeddingEvent): GenerateEmbeddingResult =
        GenerateEmbeddingQuery(event.text).let(generateEmbeddingPort::handle)
}