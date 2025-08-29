package pl.ejdev.agent.infrastructure.pubmed.service

import org.springframework.web.client.RestClient

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
            val DB_PUBMED: Pair<String, String> = "db" to "pubmed"
            val RETURN_MODE_JSON: Pair<String, String> = "retmode" to "json"
            val RETURN_MODE_TEXT: Pair<String, String> = "retmode" to "text"
            val RETURN_TYPE_FASTA: Pair<String, String> = "rettype" to "fasta"
        }
    }
}