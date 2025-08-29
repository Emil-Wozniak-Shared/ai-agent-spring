package pl.ejdev.agent.infrastructure.pubmed

import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.core.env.get
import pl.ejdev.agent.infrastructure.pubmed.adapter.GetArticlesSummariesAdapter
import pl.ejdev.agent.infrastructure.pubmed.adapter.SearchArticlesAdapter
import pl.ejdev.agent.infrastructure.pubmed.port.out.search.articlesSummary.GetArticlesSummariesPort
import pl.ejdev.agent.infrastructure.pubmed.port.out.search.articles.SearchArticlesPort
import pl.ejdev.agent.infrastructure.pubmed.service.PubmedRestClient
import pl.ejdev.agent.infrastructure.pubmed.usecase.SearchArticlesUseCase
import pl.ejdev.agent.infrastructure.pubmed.usecase.SearchSummarizeArticlesUseCase

fun BeanDefinitionDsl.pubmedBeans() {
    this.environment({ this["app.features"]?.contains("pubmed") ?: false }) {
        bean<PubmedRestClient>(name = "pubmed")
        bean<SearchArticlesPort> { SearchArticlesAdapter(ref()) }
        bean<GetArticlesSummariesPort> { GetArticlesSummariesAdapter(ref(), ref()) }
        bean<SearchArticlesUseCase>()
        bean<SearchSummarizeArticlesUseCase>()
        bean<PubmedArticlesHandler>()
    }
}