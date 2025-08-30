package pl.ejdev.agent.infrastructure.pubmed.usecase

import pl.ejdev.agent.domain.pubmed.PubmedArticle
import pl.ejdev.agent.infrastructure.base.usecase.UseCase
import pl.ejdev.agent.infrastructure.pubmed.dto.*
import pl.ejdev.agent.infrastructure.pubmed.port.out.get.articlesSummary.GetArticlesSummariesPort
import pl.ejdev.agent.infrastructure.pubmed.port.out.search.articles.SearchArticlesPort
import pl.ejdev.agent.infrastructure.pubmed.utils.orUnknown

class SearchSummarizeArticlesUseCase(
    private val searchArticlesPort: SearchArticlesPort,
    private val getArticlesSummariesPort: GetArticlesSummariesPort
) : UseCase<SearchSummarizeArticlesQuery, SearchArticleResult> {
    override fun handle(query: SearchSummarizeArticlesQuery): SearchArticleResult = query
        .toEvent()
        .let { searchArticlesPort.handle(it) }
        .let { GetArticlesSummariesEvent(it.result.idlist, query.email) }
        .let { getArticlesSummariesPort.handle(it) }
        .map { it.toPubmedArticle() }
        .let { SearchArticleResult(it) }
}

private fun ArticleResponse.toPubmedArticle(): PubmedArticle = PubmedArticle(
    title = title.orUnknown(),
    authors = this.authorsToString(),
    source = source.orUnknown(),
    pubDate = pubdate.orUnknown()
)

private fun SearchSummarizeArticlesQuery.toEvent(): SearchArticlesEvent = SearchArticlesEvent(
    query = query,
    email = email,
    maxResults = maxResults
)
