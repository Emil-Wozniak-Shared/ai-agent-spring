package pl.ejdev.agent.domain.pubmed

import com.fasterxml.jackson.annotation.JsonProperty

@Suppress("SpellCheckingInspection")
data class ESearchResult(
    @field:JsonProperty("count") var count: String? = null,
    @field:JsonProperty("retmax") var retmax: String? = null,
    @field:JsonProperty("retstart") var retstart: String? = null,
    @field:JsonProperty("idlist") var idlist: List<String> = arrayListOf(),
    @field:JsonProperty("translationset") var translationset: List<TranslationSet> = arrayListOf(),
    @field:JsonProperty("querytranslation") var querytranslation: String? = null
)