package pl.ejdev.agent.infrastructure.qdrant.dto

import org.springframework.ai.document.Document

data class SearchQdrantResult(val documents: List<Document>)