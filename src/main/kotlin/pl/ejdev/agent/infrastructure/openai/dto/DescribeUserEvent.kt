package pl.ejdev.agent.infrastructure.openai.dto

import pl.ejdev.agent.domain.pubmed.PubmedArticle

data class DescribeUserEvent(
    val articles: List<PubmedArticle>
)
