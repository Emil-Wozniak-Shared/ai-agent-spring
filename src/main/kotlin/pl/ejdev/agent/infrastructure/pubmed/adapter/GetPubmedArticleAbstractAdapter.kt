package pl.ejdev.agent.infrastructure.pubmed.adapter

import org.slf4j.LoggerFactory
import org.springframework.web.client.body
import pl.ejdev.agent.config.XmlParser
import pl.ejdev.agent.domain.pubmed.xml.PubmedArticleSet
import pl.ejdev.agent.domain.pubmed.ESearchResultResponse
import pl.ejdev.agent.infrastructure.pubmed.dto.GetPubmedArticleAbstractEvent
import pl.ejdev.agent.infrastructure.pubmed.dto.GetPubmedArticleAbstractResult
import pl.ejdev.agent.infrastructure.pubmed.port.out.get.articleAbstract.GetPubmedArticleAbstractPort
import pl.ejdev.agent.infrastructure.pubmed.service.PubmedRestClient
import pl.ejdev.agent.infrastructure.pubmed.service.PubmedRestClient.Companion.EUtils
import pl.ejdev.agent.infrastructure.pubmed.service.PubmedRestClient.Companion.Params.RETURN_MODE_JSON
import pl.ejdev.agent.utils.get
import pl.ejdev.agent.utils.queryParams
import pl.ejdev.agent.utils.uriEncode

class GetPubmedArticleAbstractAdapter(
    private val pubmed: PubmedRestClient,
    private val xmlParser: XmlParser
) : GetPubmedArticleAbstractPort {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun handle(event: GetPubmedArticleAbstractEvent): GetPubmedArticleAbstractResult {
        val searchResult = searchByTerm(event)
        val reportXml = fetchReportXml(event, searchResult)
        val body = xmlParser.parse(reportXml, PubmedArticleSet::class)
        log.info("Found article: $body")

        return body.pubmedArticle.medlineCitation.article.run {
            GetPubmedArticleAbstractResult(
                title = articleTitle,
                abstract = abstract?.abstractText,
            )
        }
    }

    private fun searchByTerm(event: GetPubmedArticleAbstractEvent): ESearchResultResponse = pubmed.client
        .get(EUtils.SEARCH) {
            queryParams(
                RETURN_MODE_JSON,
                "db" to event.db,
                "retmax" to 1,
                "usehistory" to "y",
                "term" to event.id.uriEncode(), // apparently id also works :)
            )
        }
        .retrieve()
        .body<ESearchResultResponse>() ?: error("${EUtils.SEARCH} did not return response")

    private fun fetchReportXml(
        event: GetPubmedArticleAbstractEvent,
        searchResult: ESearchResultResponse
    ): String = pubmed.client
        .get(EUtils.FETCH) {
            queryParams(
                "db" to event.db,
                "id" to searchResult.result.idlist.first()
            )
        }
        .retrieve()
        .body<String>()!!
}


