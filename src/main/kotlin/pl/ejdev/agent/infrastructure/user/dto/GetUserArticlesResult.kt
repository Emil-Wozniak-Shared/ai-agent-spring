package pl.ejdev.agent.infrastructure.user.dto

import pl.ejdev.agent.domain.pubmed.PubmedArticle

sealed interface GetUserArticlesResult {
    data class Success(val articles: List<PubmedArticle>): GetUserArticlesResult
    data class Failure(val message: String): GetUserArticlesResult
}