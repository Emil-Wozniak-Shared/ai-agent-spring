package pl.ejdev.agent.domain.pubmed.xml

import com.fasterxml.jackson.annotation.JsonProperty

data class PublicationTypeList(
    @field:JsonProperty("PublicationType")
    val publicationType: PublicationType
)