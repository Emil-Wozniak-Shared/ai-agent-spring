package pl.ejdev.agent.infrastructure.documents.usecase.create

import pl.ejdev.agent.infrastructure.documents.dto.CreateDocumentQuery
import pl.ejdev.agent.infrastructure.documents.dto.CreateDocumentResult
import pl.ejdev.agent.infrastructure.base.usecase.UseCase

interface CreateDocumentUseCase : UseCase<CreateDocumentQuery, CreateDocumentResult>