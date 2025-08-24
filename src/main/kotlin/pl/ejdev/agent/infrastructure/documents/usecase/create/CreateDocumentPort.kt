package pl.ejdev.agent.infrastructure.documents.usecase.create

import pl.ejdev.agent.infrastructure.documents.dto.CreateDocumentQuery
import pl.ejdev.agent.infrastructure.documents.dto.CreateDocumentResult
import pl.ejdev.agent.infrastructure.base.usecase.UseCase
import pl.ejdev.agent.infrastructure.qdrant.dto.CreateQdrantEvent
import pl.ejdev.agent.infrastructure.qdrant.port.out.CreateQdrantPort

class CreateDocumentUseCase(
    private val createQdrantPort: CreateQdrantPort
) : UseCase<CreateDocumentQuery, CreateDocumentResult> {
    override fun handle(query: CreateDocumentQuery): CreateDocumentResult = createQdrantPort
         .handle(CreateQdrantEvent(query.documents))
         .let { CreateDocumentResult }
}