package pl.ejdev.agent.infrastructure.pubmed.dto

data class SearchArticlesEvent(
    val query: String,
    val email: String,
    val maxResults: Int
)