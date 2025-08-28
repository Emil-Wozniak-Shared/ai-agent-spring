package pl.ejdev.agent.infrastructure.pubmed.dto

import pl.ejdev.agent.domain.pubmed.PubmedArticle

data class SearchArticleResult(
    val articles: List<PubmedArticle>
) {
    companion object {
        val EMPTY = SearchArticleResult(listOf())
    }
}