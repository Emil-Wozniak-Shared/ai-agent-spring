package pl.ejdev.agent.infrastructure.pubmed.dto

data class GetPubmedArticleAbstractEvent(
    val db: String,
    val id: String
)