package pl.ejdev.agent.infrastructure.pubmed.dto

import com.fasterxml.jackson.annotation.JsonProperty
import pl.ejdev.agent.domain.pubmed.ArticleIds
import pl.ejdev.agent.domain.pubmed.Authors
import pl.ejdev.agent.domain.pubmed.History
import pl.ejdev.agent.infrastructure.pubmed.utils.orUnknown

data class ArticleResponse(
    @field:JsonProperty("uid") var uid: String? = null,
    @field:JsonProperty("pubdate") var pubdate: String? = null,
    @field:JsonProperty("epubdate") var epubdate: String? = null,
    @field:JsonProperty("source") var source: String? = null,
    @field:JsonProperty("authors") var authors: List<Authors> = arrayListOf(),
    @field:JsonProperty("lastauthor") var lastauthor: String? = null,
    @field:JsonProperty("title") var title: String? = null,
    @field:JsonProperty("sorttitle") var sorttitle: String? = null,
    @field:JsonProperty("volume") var volume: String? = null,
    @field:JsonProperty("issue") var issue: String? = null,
    @field:JsonProperty("pages") var pages: String? = null,
    @field:JsonProperty("lang") var lang: List<String> = arrayListOf(),
    @field:JsonProperty("nlmuniqueid") var nlmuniqueid: String? = null,
    @field:JsonProperty("issn") var issn: String? = null,
    @field:JsonProperty("essn") var essn: String? = null,
    @field:JsonProperty("pubtype") var pubtype: List<String> = arrayListOf(),
    @field:JsonProperty("recordstatus") var recordstatus: String? = null,
    @field:JsonProperty("pubstatus") var pubstatus: String? = null,
    @field:JsonProperty("articleids") var articleids: List<ArticleIds> = arrayListOf(),
    @field:JsonProperty("history") var history: List<History> = arrayListOf(),
    @field:JsonProperty("references") var references: List<String> = arrayListOf(),
    @field:JsonProperty("attributes") var attributes: List<String> = arrayListOf(),
    @field:JsonProperty("pmcrefcount") var pmcrefcount: String? = null,
    @field:JsonProperty("fulljournalname") var fulljournalname: String? = null,
    @field:JsonProperty("elocationid") var elocationid: String? = null,
    @field:JsonProperty("doctype") var doctype: String? = null,
    @field:JsonProperty("srccontriblist") var srccontriblist: List<String> = arrayListOf(),
    @field:JsonProperty("booktitle") var booktitle: String? = null,
    @field:JsonProperty("medium") var medium: String? = null,
    @field:JsonProperty("edition") var edition: String? = null,
    @field:JsonProperty("publisherlocation") var publisherlocation: String? = null,
    @field:JsonProperty("publishername") var publishername: String? = null,
    @field:JsonProperty("srcdate") var srcdate: String? = null,
    @field:JsonProperty("reportnumber") var reportnumber: String? = null,
    @field:JsonProperty("availablefromurl") var availablefromurl: String? = null,
    @field:JsonProperty("locationlabel") var locationlabel: String? = null,
    @field:JsonProperty("doccontriblist") var doccontriblist: List<String> = arrayListOf(),
    @field:JsonProperty("docdate") var docdate: String? = null,
    @field:JsonProperty("bookname") var bookname: String? = null,
    @field:JsonProperty("chapter") var chapter: String? = null,
    @field:JsonProperty("sortpubdate") var sortpubdate: String? = null,
    @field:JsonProperty("sortfirstauthor") var sortfirstauthor: String? = null,
    @field:JsonProperty("vernaculartitle") var vernaculartitle: String? = null
) {

    fun authorsToString() = this.authors.joinToString(", ") { it.name.orUnknown() }
}