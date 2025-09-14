package pl.ejdev.agent.infrastructure.pubmed.port.out.repository

import pl.ejdev.agent.domain.pubmed.PubmedArticle
import pl.ejdev.agent.infrastructure.pubmed.dao.ArticleEntity

interface PubmedArticleRepository {
    fun findAll(email: String): List<ArticleEntity>
    fun addAll(email: String, articles: List<PubmedArticle>)
    fun allArticlesByIds(ids: List<String>): List<ArticleEntity>
    fun allArticleNotByIds(ids: List<String>): List<ArticleEntity>
}