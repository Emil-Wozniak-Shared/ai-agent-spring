package pl.ejdev.agent.infrastructure.qdrant.usecase.create

import pl.ejdev.agent.infrastructure.base.usecase.UseCase
import pl.ejdev.agent.infrastructure.qdrant.dto.CreateQdrantEvent
import pl.ejdev.agent.infrastructure.qdrant.dto.CreateQdrantQuery
import pl.ejdev.agent.infrastructure.qdrant.dto.CreateQdrantResult
import pl.ejdev.agent.infrastructure.qdrant.dto.SearchQdrantEvent
import pl.ejdev.agent.infrastructure.qdrant.dto.SearchQdrantQuery
import pl.ejdev.agent.infrastructure.qdrant.dto.SearchQdrantResult
import pl.ejdev.agent.infrastructure.qdrant.port.out.CreateQdrantPort
import pl.ejdev.agent.infrastructure.qdrant.port.out.SearchQdrantPort

class CreateQdrantUseCase(
    private val createQdrantPort: CreateQdrantPort
) : UseCase<CreateQdrantQuery, CreateQdrantResult> {
    override fun handle(query: CreateQdrantQuery): CreateQdrantResult =
        query.toEvent().let(createQdrantPort::handle)

    private fun CreateQdrantQuery.toEvent() = CreateQdrantEvent(documents)
}