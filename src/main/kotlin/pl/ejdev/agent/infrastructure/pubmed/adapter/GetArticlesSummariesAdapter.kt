package pl.ejdev.agent.infrastructure.pubmed.adapter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.web.client.body
import pl.ejdev.agent.infrastructure.pubmed.dto.ArticleResponse
import pl.ejdev.agent.infrastructure.pubmed.dto.GetArticleSummaryResponse
import pl.ejdev.agent.infrastructure.pubmed.dto.GetArticlesSummariesEvent
import pl.ejdev.agent.infrastructure.pubmed.port.out.get.articlesSummary.GetArticlesSummariesPort
import pl.ejdev.agent.infrastructure.pubmed.service.PubmedRestClient
import pl.ejdev.agent.infrastructure.pubmed.service.PubmedRestClient.Companion.EUtils
import pl.ejdev.agent.infrastructure.pubmed.service.PubmedRestClient.Companion.Params
import pl.ejdev.agent.utils.get
import pl.ejdev.agent.utils.queryParams
import pl.ejdev.agent.utils.uriEncode

class GetArticlesSummariesAdapter(
    private val pubmed: PubmedRestClient,
    private val objectMapper: ObjectMapper
): GetArticlesSummariesPort {
    override fun handle(event: GetArticlesSummariesEvent): List<ArticleResponse> {
        val ids = event.idList.joinToString(",")
        val getArticleSummaryResponse = articleSummaryResponse(ids, event)
        return articleResponses(getArticleSummaryResponse)
    }

    private fun articleResponses(getArticleSummaryResponse: GetArticleSummaryResponse): List<ArticleResponse> =
        getArticleSummaryResponse.result
            .asSequence()
            .filter { it.key != "uids" }
            .map { objectMapper.writeValueAsString(it.value) }
            .map { objectMapper.readValue<ArticleResponse>(it) }
            .toList()

    private fun articleSummaryResponse(
        ids: String,
        event: GetArticlesSummariesEvent
    ): GetArticleSummaryResponse = (pubmed.client
        .get(EUtils.SUMMARY) {
            queryParams(
                Params.DB_PUBMED,
                Params.RETURN_MODE_JSON,
                "id" to ids,
                "email" to event.email.uriEncode(),
            )
        }
        .retrieve()
        .body<GetArticleSummaryResponse>()
        ?: GetArticleSummaryResponse())
}