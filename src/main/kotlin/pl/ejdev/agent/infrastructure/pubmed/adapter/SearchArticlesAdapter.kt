package pl.ejdev.agent.infrastructure.pubmed.adapter

import org.springframework.web.client.body
import pl.ejdev.agent.domain.pubmed.ESearchResultResponse
import pl.ejdev.agent.infrastructure.pubmed.dto.SearchArticlesEvent
import pl.ejdev.agent.infrastructure.pubmed.port.out.search.articles.SearchArticlesPort
import pl.ejdev.agent.infrastructure.pubmed.service.PubmedRestClient
import pl.ejdev.agent.infrastructure.pubmed.service.PubmedRestClient.Companion.EUtils
import pl.ejdev.agent.infrastructure.pubmed.service.PubmedRestClient.Companion.Params
import pl.ejdev.agent.infrastructure.pubmed.service.PubmedRestClient.Companion.QueryKeys
import pl.ejdev.agent.utils.get
import pl.ejdev.agent.utils.queryParams
import pl.ejdev.agent.utils.uriEncode

class SearchArticlesAdapter(
    private val pubmed: PubmedRestClient,
) : SearchArticlesPort {
    override fun handle(event: SearchArticlesEvent): ESearchResultResponse = pubmed
        .client
        .get(EUtils.SEARCH) {
            queryParams(
                Params.DB_PUBMED,
                Params.RETURN_MODE_JSON,
                QueryKeys.TERM to event.query.uriEncode(),
                QueryKeys.EMAIL to event.email.uriEncode(),
                QueryKeys.RESULT_MAX to event.maxResults
            )
        }
        .retrieve()
        .runCatching { body<ESearchResultResponse>() }
        .getOrElse { ESearchResultResponse.Empty }!!
}