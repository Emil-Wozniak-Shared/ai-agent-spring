package pl.ejdev.agent.infrastructure.pubmed

import org.springframework.context.support.BeanDefinitionDsl
import pl.ejdev.agent.infrastructure.pubmed.adapter.GetArticlesSummariesAdapter
import pl.ejdev.agent.infrastructure.pubmed.adapter.GetPubmedArticleAbstractAdapter
import pl.ejdev.agent.infrastructure.pubmed.adapter.SearchArticlesAdapter
import pl.ejdev.agent.infrastructure.pubmed.port.out.get.articleAbstract.GetPubmedArticleAbstractPort
import pl.ejdev.agent.infrastructure.pubmed.port.out.get.articlesSummary.GetArticlesSummariesPort
import pl.ejdev.agent.infrastructure.pubmed.port.out.search.articles.SearchArticlesPort
import pl.ejdev.agent.infrastructure.pubmed.service.PubmedRestClient
import pl.ejdev.agent.infrastructure.pubmed.usecase.GetPubmedArticleAbstractUseCase
import pl.ejdev.agent.infrastructure.pubmed.usecase.SearchArticlesUseCase
import pl.ejdev.agent.infrastructure.pubmed.usecase.SearchSummarizeArticlesUseCase
import pl.ejdev.agent.utils.features

fun BeanDefinitionDsl.pubmedBeans() {
    this.environment({ features.pubmed }) {
        bean<PubmedRestClient>(name = "pubmed")
        bean<SearchArticlesPort> { SearchArticlesAdapter(ref()) }
        bean<GetArticlesSummariesPort> { GetArticlesSummariesAdapter(ref(), ref()) }
        bean<GetPubmedArticleAbstractPort> { GetPubmedArticleAbstractAdapter(ref(), ref()) }
        bean<SearchArticlesUseCase>()
        bean<SearchSummarizeArticlesUseCase>()
        bean<GetPubmedArticleAbstractUseCase>()
        bean<PubmedArticlesHandler>()
    }
}