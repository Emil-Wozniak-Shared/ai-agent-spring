package pl.ejdev.agent.domain.pubmed.xml

import com.fasterxml.jackson.annotation.JsonProperty

data class PubmedArticleXML(
    @field:JsonProperty("MedlineCitation")
    val medlineCitation: MedlineCitationXML,

    @field:JsonProperty("PubmedData")
    val pubmedData: PubmedData
)