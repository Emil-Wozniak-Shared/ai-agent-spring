package pl.ejdev.agent.infrastructure.documents.usecase.search

import pl.ejdev.agent.infrastructure.documents.dto.SearchDocumentQuery
import pl.ejdev.agent.infrastructure.documents.dto.SearchDocumentResult
import pl.ejdev.agent.infrastructure.base.usecase.UseCase

interface SearchDocumentUseCase : UseCase<SearchDocumentQuery, List<SearchDocumentResult>>