package pl.ejdev.agent.domain.pubmed.xml

import com.fasterxml.jackson.annotation.JsonProperty

data class MedlineCitationXML(
    @field:JsonProperty("PMID")
    val pmid: Pmid,

    @field:JsonProperty("Article")
    val article: ArticleXML,
)