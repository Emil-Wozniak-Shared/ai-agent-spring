package pl.ejdev.agent.infrastructure.pubmed.adapter

import pl.ejdev.agent.infrastructure.pubmed.port.out.repository.PubmedArticleRepository
import pl.ejdev.agent.infrastructure.user.dto.AddUserArticlesEvent
import pl.ejdev.agent.infrastructure.user.dto.AddUserArticlesResult
import pl.ejdev.agent.infrastructure.pubmed.port.out.add.AddUserArticlesPort

class AddUserArticlesAdapter(
    private val pubmedArticleRepository: PubmedArticleRepository
) : AddUserArticlesPort {
    override fun handle(event: AddUserArticlesEvent): AddUserArticlesResult = pubmedArticleRepository
        .runCatching { addAll(event.email, event.articles) }
        .map { AddUserArticlesResult.Success }
        .getOrElse { AddUserArticlesResult.Failure("${it.message}") }
}