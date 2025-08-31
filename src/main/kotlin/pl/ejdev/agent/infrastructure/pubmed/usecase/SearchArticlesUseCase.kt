package pl.ejdev.agent.infrastructure.pubmed.usecase

import pl.ejdev.agent.domain.pubmed.PubmedArticle
import pl.ejdev.agent.infrastructure.base.usecase.UseCase
import pl.ejdev.agent.infrastructure.pubmed.dto.ArticleResponse
import pl.ejdev.agent.infrastructure.pubmed.dto.GetArticlesSummariesEvent
import pl.ejdev.agent.infrastructure.pubmed.dto.SearchArticleResult
import pl.ejdev.agent.infrastructure.pubmed.dto.SearchArticlesQuery
import pl.ejdev.agent.infrastructure.pubmed.port.out.get.articlesSummary.GetArticlesSummariesPort
import pl.ejdev.agent.infrastructure.pubmed.utils.orUnknown

class SearchArticlesUseCase(
    private val getArticlesSummariesPort: GetArticlesSummariesPort
) : UseCase<SearchArticlesQuery, SearchArticleResult> {

    override fun handle(query: SearchArticlesQuery): SearchArticleResult = query
        .let { GetArticlesSummariesEvent(it.ids, it.email) }
        .let(getArticlesSummariesPort::handle)
        .map { it.toPubmedArticle() }
        .let(::SearchArticleResult)
}

private fun ArticleResponse.toPubmedArticle() = PubmedArticle(
    id = uid,
    title = title.orUnknown(),
    authors = authorsToString(),
    source = source.orUnknown(),
    pubDate = pubdate.orUnknown()
)
