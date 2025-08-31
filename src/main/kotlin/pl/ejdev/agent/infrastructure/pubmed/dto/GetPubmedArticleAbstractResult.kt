package pl.ejdev.agent.infrastructure.pubmed.dto

data class GetPubmedArticleAbstractResult(
    val title: String? = null,
    val abstract: String? = null,
    val language: String? = null,
)