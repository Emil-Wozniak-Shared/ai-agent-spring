package pl.ejdev.agent.domain.pubmed.xml

import com.fasterxml.jackson.annotation.JsonProperty

data class ArticleXML(
    @field:JsonProperty("ArticleTitle")
    val articleTitle: String? = null,

    @field:JsonProperty("ELocationID")
    val eLocationID: ELocationID,

    @field:JsonProperty("Abstract")
    val abstract: Abstract? = null,

    @field:JsonProperty("Language")
    val language: String? = null,

    @field:JsonProperty("PublicationTypeList")
    val publicationTypeList: PublicationTypeList
)