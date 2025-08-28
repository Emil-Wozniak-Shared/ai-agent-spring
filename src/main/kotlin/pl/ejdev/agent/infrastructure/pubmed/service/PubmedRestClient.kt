package pl.ejdev.agent.infrastructure.pubmed.service

import org.springframework.web.client.RestClient

data class PubmedRestClient(
    val client: RestClient = RestClient.create("https://eutils.ncbi.nlm.nih.gov/entrez")
)