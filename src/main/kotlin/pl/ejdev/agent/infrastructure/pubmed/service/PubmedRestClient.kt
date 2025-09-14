package pl.ejdev.agent.infrastructure.pubmed.service

import org.springframework.web.client.RestClient

@Suppress("SpellCheckingInspection")
data class PubmedRestClient(
    val client: RestClient = RestClient.create("https://eutils.ncbi.nlm.nih.gov/entrez")
) {
    companion object {
        object EUtils {
            const val SUMMARY: String = "/eutils/esummary.fcgi"
            const val SEARCH: String = "/eutils/esearch.fcgi"
            const val FETCH: String = "/eutils/efetch.fcgi"
        }

        object Params {
            val DB_PUBMED: Pair<String, String> = "db" to DB_NAME
            val RETURN_MODE_JSON: Pair<String, String> = "retmode" to "json"
        }

        object QueryKeys {
            const val RESULT_MAX = "retmax"
            const val EMAIL = "email"
            const val TERM = "term"
        }

        const val DB_NAME = "pubmed"
    }
}