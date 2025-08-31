package pl.ejdev.agent.infrastructure.documents.dto

data class SearchDocumentQuery(
    val query: String,
    val limit: Int = 10,
    val threshold: Double = 0.7,
    val keywords: List<Keyword>
) {
    data class Keyword(
        val key: String,
        val value: String
    )
}