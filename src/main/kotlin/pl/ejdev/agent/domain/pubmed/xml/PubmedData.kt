package pl.ejdev.agent.domain.pubmed.xml

import com.fasterxml.jackson.annotation.JsonProperty

data class PubmedData(

    @field:JsonProperty("PublicationStatus")
    val publicationStatus: String? = null,

    @field:JsonProperty("ArticleIdList")
    val articleIDList: ArticleIDList
)