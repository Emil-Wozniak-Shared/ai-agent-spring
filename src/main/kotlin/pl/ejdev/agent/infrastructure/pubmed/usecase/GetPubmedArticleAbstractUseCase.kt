package pl.ejdev.agent.infrastructure.pubmed.usecase

import pl.ejdev.agent.infrastructure.base.usecase.UseCase
import pl.ejdev.agent.infrastructure.pubmed.dto.GetPubmedArticleAbstractEvent
import pl.ejdev.agent.infrastructure.pubmed.dto.GetPubmedArticleAbstractQuery
import pl.ejdev.agent.infrastructure.pubmed.dto.GetPubmedArticleAbstractResult
import pl.ejdev.agent.infrastructure.pubmed.port.out.get.articleAbstract.GetPubmedArticleAbstractPort

class GetPubmedArticleAbstractUseCase(
    private val getPubmedArticleAbstractPort: GetPubmedArticleAbstractPort
) : UseCase<GetPubmedArticleAbstractQuery, GetPubmedArticleAbstractResult> {
    override fun handle(query: GetPubmedArticleAbstractQuery): GetPubmedArticleAbstractResult =
        query
            .let { GetPubmedArticleAbstractEvent(db = it.db, id = it.id) }
            .let { getPubmedArticleAbstractPort.handle(it) }
}