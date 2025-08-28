package pl.ejdev.agent.infrastructure.pubmed.adapter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.web.client.body
import pl.ejdev.agent.infrastructure.pubmed.dto.ArticleResponse
import pl.ejdev.agent.infrastructure.pubmed.dto.GetArticleSummaryResponse
import pl.ejdev.agent.infrastructure.pubmed.dto.GetArticlesSummariesEvent
import pl.ejdev.agent.infrastructure.pubmed.port.out.search.articles.GetArticlesSummariesPort
import pl.ejdev.agent.infrastructure.pubmed.service.PubmedRestClient
import pl.ejdev.agent.infrastructure.pubmed.utils.DB_PUBMED
import pl.ejdev.agent.infrastructure.pubmed.utils.RETURN_MODE_JSON
import pl.ejdev.agent.utils.get
import pl.ejdev.agent.utils.queryParams
import pl.ejdev.agent.utils.uriEncode

class GetArticlesSummariesAdapter(
    private val pubmed: PubmedRestClient,
    private val objectMapper: ObjectMapper
): GetArticlesSummariesPort {
    override fun handle(event: GetArticlesSummariesEvent): List<ArticleResponse> {
        val ids = event.idList.joinToString(",")
        val getArticleSummaryResponse = pubmed.client
            .get("/eutils/esummary.fcgi") {
                queryParams(
                    DB_PUBMED,
                    RETURN_MODE_JSON,
                    "id" to ids,
                    "email" to event.email.uriEncode(),
                )
            }
            .retrieve()
            .body<GetArticleSummaryResponse>()
            ?: GetArticleSummaryResponse()
        return getArticleSummaryResponse.result
            .asSequence()
            .filter { it.key != "uids" }
            .map { objectMapper.writeValueAsString(it.value) }
            .map { objectMapper.readValue<ArticleResponse>(it)  }
            .toList()
    }
}