package pl.ejdev.agent.infrastructure.pubmed.usecase

import org.springframework.security.core.context.SecurityContextHolder
import pl.ejdev.agent.domain.pubmed.PubmedArticle
import pl.ejdev.agent.infrastructure.base.usecase.UseCase
import pl.ejdev.agent.infrastructure.pubmed.dto.*
import pl.ejdev.agent.infrastructure.pubmed.port.out.get.articlesSummary.GetArticlesSummariesPort
import pl.ejdev.agent.infrastructure.pubmed.port.out.search.articles.SearchArticlesPort
import pl.ejdev.agent.infrastructure.pubmed.utils.orUnknown
import pl.ejdev.agent.infrastructure.user.dto.AddUserArticlesEvent
import pl.ejdev.agent.infrastructure.user.port.`in`.AddUserArticlesPort
import pl.ejdev.agent.security.utils.userEntity

class SearchSummarizeArticlesUseCase(
    private val searchArticlesPort: SearchArticlesPort,
    private val getArticlesSummariesPort: GetArticlesSummariesPort,
    private val addUserArticlesPort: AddUserArticlesPort
) : UseCase<SearchSummarizeArticlesQuery, SearchArticleResult> {
    override fun handle(query: SearchSummarizeArticlesQuery): SearchArticleResult = query
        .toEvent()
        .let { searchArticlesPort.handle(it) }
        .let { GetArticlesSummariesEvent(it.result.idlist, query.email) }
        .let { getArticlesSummariesPort.handle(it) }
        .map { it.toPubmedArticle() }
        .also { articles ->
            if (query.email == SecurityContextHolder.getContext().userEntity.email) {
                addUserArticlesPort.handle(AddUserArticlesEvent(query.email, articles))
            }
        }
        .let { SearchArticleResult(it) }
}

private fun ArticleResponse.toPubmedArticle(): PubmedArticle = PubmedArticle(
    id = uid,
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
