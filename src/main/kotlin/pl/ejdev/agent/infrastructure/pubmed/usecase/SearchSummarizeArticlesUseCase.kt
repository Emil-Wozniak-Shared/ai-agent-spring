package pl.ejdev.agent.infrastructure.pubmed.usecase

import pl.ejdev.agent.domain.pubmed.PubmedArticle
import pl.ejdev.agent.infrastructure.base.usecase.UseCase
import pl.ejdev.agent.infrastructure.orcid.dto.FindOrcidEvent
import pl.ejdev.agent.infrastructure.orcid.dto.FindOrcidResult
import pl.ejdev.agent.infrastructure.orcid.port.`in`.FindOrcidPort
import pl.ejdev.agent.infrastructure.pubmed.dto.*
import pl.ejdev.agent.infrastructure.pubmed.error.NotCurrentUserOrcidError
import pl.ejdev.agent.infrastructure.pubmed.port.out.add.AddUserArticlesPort
import pl.ejdev.agent.infrastructure.pubmed.port.out.all.GetUserArticlesPort
import pl.ejdev.agent.infrastructure.pubmed.port.out.get.articlesSummary.GetArticlesSummariesPort
import pl.ejdev.agent.infrastructure.pubmed.port.out.search.articles.SearchArticlesPort
import pl.ejdev.agent.infrastructure.pubmed.utils.orUnknown
import pl.ejdev.agent.infrastructure.user.dto.AddUserArticlesEvent
import pl.ejdev.agent.infrastructure.user.dto.GetUserArticlesEvent
import pl.ejdev.agent.infrastructure.user.dto.GetUserArticlesResult

class SearchSummarizeArticlesUseCase(
    private val searchArticlesPort: SearchArticlesPort,
    private val getArticlesSummariesPort: GetArticlesSummariesPort,
    private val addUserArticlesPort: AddUserArticlesPort,
    private val getUserArticlesPort: GetUserArticlesPort,
    private val findOrcidPort: FindOrcidPort
) : UseCase<SearchSummarizeArticlesQuery, SearchArticleResult> {

    override fun handle(query: SearchSummarizeArticlesQuery): SearchArticleResult {
        val pubmedArticles = when (val orcidResult = findOrcid(query)) {
            is FindOrcidResult.Success -> getCurrentUserArticles(orcidResult, query)
            is FindOrcidResult.Failure -> requestPubmedArticles(query)
        }
        return SearchArticleResult(pubmedArticles)
    }

    private fun findOrcid(query: SearchSummarizeArticlesQuery): FindOrcidResult =
        findOrcidPort.handle(FindOrcidEvent(query.email))

    private fun getCurrentUserArticles(
        orcidResult: FindOrcidResult.Success,
        query: SearchSummarizeArticlesQuery
    ): List<PubmedArticle> =
        if (orcidResult.id == query.query) {
            val articles = when (val result = fetchUserArticles(query)) {
                is GetUserArticlesResult.Success -> result.articles
                is GetUserArticlesResult.Failure -> listOf()
            }
            articles.ifEmpty { requestPubmedArticles(query).also { persist(query, articles) } }
        } else {
            throw NotCurrentUserOrcidError(query.query)
        }

    private fun fetchUserArticles(query: SearchSummarizeArticlesQuery): GetUserArticlesResult =
        getUserArticlesPort.handle(GetUserArticlesEvent(query.email))

    private fun persist(query: SearchSummarizeArticlesQuery, articles: List<PubmedArticle>) {
        addUserArticlesPort.handle(AddUserArticlesEvent(query.email, articles))
    }

    private fun requestPubmedArticles(query: SearchSummarizeArticlesQuery): List<PubmedArticle> = query
        .toEvent()
        .let { searchArticlesPort.handle(it) }
        .let { GetArticlesSummariesEvent(it.result.idlist, query.email) }
        .let { getArticlesSummariesPort.handle(it) }
        .map { it.toPubmedArticle() }
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
