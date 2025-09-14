package pl.ejdev.agent.infrastructure.pubmed.adapter

import pl.ejdev.agent.domain.pubmed.PubmedArticle
import pl.ejdev.agent.infrastructure.pubmed.port.out.repository.PubmedArticleRepository
import pl.ejdev.agent.infrastructure.user.dto.GetUserArticlesEvent
import pl.ejdev.agent.infrastructure.user.dto.GetUserArticlesResult
import pl.ejdev.agent.infrastructure.pubmed.port.out.all.GetUserArticlesPort

class GetUserArticlesAdapter(
    private val pubmedArticleRepository: PubmedArticleRepository
) : GetUserArticlesPort {
    override fun handle(event: GetUserArticlesEvent): GetUserArticlesResult =
        pubmedArticleRepository.findAll(event.email)
            .map { PubmedArticle(it.pubmedId, it.title, it.authors, it.source, it.pubDate) }
            .takeIf { it.isNotEmpty() }
            ?.let { GetUserArticlesResult.Success(it) }
            ?: GetUserArticlesResult.Failure("No articles found")
}