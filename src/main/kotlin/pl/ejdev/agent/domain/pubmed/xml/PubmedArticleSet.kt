package pl.ejdev.agent.domain.pubmed.xml

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("PubmedArticleSet")
data class PubmedArticleSet(
    @field:JsonProperty("PubmedArticle")
    val pubmedArticle: PubmedArticleXML
)