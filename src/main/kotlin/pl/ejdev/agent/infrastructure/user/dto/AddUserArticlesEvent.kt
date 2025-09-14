package pl.ejdev.agent.infrastructure.user.dto

import pl.ejdev.agent.domain.pubmed.PubmedArticle

data class AddUserArticlesEvent(
    val email: String,
    val articles: List<PubmedArticle>
)
