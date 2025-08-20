package pl.ejdev.agent.infrastructure.documents.dto

import pl.ejdev.agent.domain.Document

data class CreateDocumentQuery(
   val documents: List<Document>
)