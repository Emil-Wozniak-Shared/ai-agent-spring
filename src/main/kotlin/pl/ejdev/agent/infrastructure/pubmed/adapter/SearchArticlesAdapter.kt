package pl.ejdev.agent.infrastructure.pubmed.adapter

import org.springframework.web.client.body
import pl.ejdev.agent.domain.pubmed.ESearchResultResponse
import pl.ejdev.agent.infrastructure.pubmed.dto.SearchArticlesEvent
import pl.ejdev.agent.infrastructure.pubmed.port.out.search.articles.SearchArticlesPort
import pl.ejdev.agent.infrastructure.pubmed.service.PubmedRestClient
import pl.ejdev.agent.infrastructure.pubmed.service.PubmedRestClient.Companion.EUtils
import pl.ejdev.agent.infrastructure.pubmed.service.PubmedRestClient.Companion.Params
import pl.ejdev.agent.utils.get
import pl.ejdev.agent.utils.queryParams
import pl.ejdev.agent.utils.uriEncode

class SearchArticlesAdapter(
    private val pubmed: PubmedRestClient,
) : SearchArticlesPort {
    override fun handle(event: SearchArticlesEvent): ESearchResultResponse = pubmed.client
        .get(EUtils.SEARCH) {
            queryParams(
                Params.DB_PUBMED,
                Params.RETURN_MODE_JSON,
                "term" to event.query.uriEncode(),
                "email" to event.email.uriEncode(),
                "retmax" to event.maxResults
            )
        }
        .retrieve()
        .body<ESearchResultResponse>()
        ?: ESearchResultResponse()
}