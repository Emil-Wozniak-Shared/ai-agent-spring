package pl.ejdev.agent.infrastructure.pubmed.dto

data class GetPubmedArticleAbstractResult(
    val articleTitle: String? = null,
    val abstractText: String? = null,
    val language: String? = null,
)