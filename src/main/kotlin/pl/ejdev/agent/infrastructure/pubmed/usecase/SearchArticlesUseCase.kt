package pl.ejdev.agent.infrastructure.pubmed.usecase

import pl.ejdev.agent.domain.pubmed.PubmedArticle
import pl.ejdev.agent.infrastructure.base.usecase.UseCase
import pl.ejdev.agent.infrastructure.pubmed.dto.*
import pl.ejdev.agent.infrastructure.pubmed.port.out.search.articles.GetArticlesSummariesPort
import pl.ejdev.agent.infrastructure.pubmed.port.out.search.articles.SearchArticlesPort

private const val UNKNOWN = "N/A"

class SearchArticlesUseCase(
    private val searchArticlesPort: SearchArticlesPort,
    private val getArticlesSummariesPort: GetArticlesSummariesPort
) : UseCase<SearchArticlesQuery, SearchArticleResult> {
    override fun handle(query: SearchArticlesQuery): SearchArticleResult = query
        .toEvent()
        .let { searchArticlesPort.handle(it) }
        .let { GetArticlesSummariesEvent(it.esearchresult.idlist, query.email) }
        .let { getArticlesSummariesPort.handle(it) }
        .map { it.toPubmedArticle() }
        .let { SearchArticleResult(it) }

}

private fun ArticleResponse.toPubmedArticle(): PubmedArticle = PubmedArticle(
    title = title ?: UNKNOWN,
    authors = authors.joinToString(", ") { it.name ?: UNKNOWN },
    source = source ?: UNKNOWN,
    pubDate = pubdate ?: UNKNOWN
)

private fun SearchArticlesQuery.toEvent(): SearchArticlesEvent = SearchArticlesEvent(
    query = query,
    email = email,
    maxResults = maxResults
)
