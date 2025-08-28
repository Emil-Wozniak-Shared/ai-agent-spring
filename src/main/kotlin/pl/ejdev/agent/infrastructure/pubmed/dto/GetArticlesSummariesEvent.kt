package pl.ejdev.agent.infrastructure.pubmed.dto

data class GetArticlesSummariesEvent(
    val idList: List<String>,
    val email: String
)