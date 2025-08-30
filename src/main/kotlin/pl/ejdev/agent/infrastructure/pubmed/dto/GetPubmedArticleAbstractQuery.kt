package pl.ejdev.agent.infrastructure.pubmed.dto

import org.springframework.web.servlet.function.ServerRequest
import pl.ejdev.agent.infrastructure.pubmed.service.PubmedRestClient.Companion.DB_NAME
import kotlin.jvm.optionals.getOrElse

data class GetPubmedArticleAbstractQuery(
    val id: String,
    val db: String
) {
    companion object {
        fun from(request: ServerRequest) = GetPubmedArticleAbstractQuery(
            id = request.pathVariable("id"),
            db = request.param("db").getOrElse { DB_NAME }
        )
    }
}