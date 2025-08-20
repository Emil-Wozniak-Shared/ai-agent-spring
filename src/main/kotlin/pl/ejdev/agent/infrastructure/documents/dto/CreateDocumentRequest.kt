package pl.ejdev.agent.infrastructure.documents.dto

import pl.ejdev.agent.domain.Document

data class CreateDocumentRequest(
    val documents: List<Document>
)