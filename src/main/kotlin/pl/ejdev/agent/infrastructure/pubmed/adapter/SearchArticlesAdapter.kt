package pl.ejdev.agent.infrastructure.pubmed.adapter

import org.springframework.web.client.body
import pl.ejdev.agent.infrastructure.pubmed.dto.ESearchResultResponse
import pl.ejdev.agent.infrastructure.pubmed.dto.SearchArticlesEvent
import pl.ejdev.agent.infrastructure.pubmed.port.out.search.articles.SearchArticlesPort
import pl.ejdev.agent.infrastructure.pubmed.service.PubmedRestClient
import pl.ejdev.agent.infrastructure.pubmed.utils.DB_PUBMED
import pl.ejdev.agent.infrastructure.pubmed.utils.RETURN_MODE_JSON
import pl.ejdev.agent.utils.get
import pl.ejdev.agent.utils.queryParams
import pl.ejdev.agent.utils.uriEncode

class SearchArticlesAdapter(
    private val pubmed: PubmedRestClient,
) : SearchArticlesPort {
    override fun handle(event: SearchArticlesEvent): ESearchResultResponse = pubmed.client
        .get("/eutils/esearch.fcgi") {
            queryParams(
                DB_PUBMED,
                RETURN_MODE_JSON,
                "term" to event.query.uriEncode(),
                "email" to event.email.uriEncode(),
                "retmax" to event.maxResults
            )
        }
        .retrieve()
        .body<ESearchResultResponse>()
        ?: ESearchResultResponse()
}