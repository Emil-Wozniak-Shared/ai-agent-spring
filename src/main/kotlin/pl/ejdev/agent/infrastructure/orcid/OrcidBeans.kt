package pl.ejdev.agent.infrastructure.orcid

import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.core.env.get
import pl.ejdev.agent.infrastructure.pubmed.adapter.GetPubmedArticleAbstractAdapter
import pl.ejdev.agent.infrastructure.pubmed.adapter.GetArticlesSummariesAdapter
import pl.ejdev.agent.infrastructure.orcid.adapter.OrcidProfileRepositoryImpl
import pl.ejdev.agent.infrastructure.pubmed.adapter.SearchArticlesAdapter
import pl.ejdev.agent.infrastructure.orcid.adapter.UpdateOrcidAdapter
import pl.ejdev.agent.infrastructure.orcid.port.`in`.UpdateOrcidPort
import pl.ejdev.agent.infrastructure.pubmed.port.out.get.articleAbstract.GetPubmedArticleAbstractPort
import pl.ejdev.agent.infrastructure.pubmed.port.out.get.articlesSummary.GetArticlesSummariesPort
import pl.ejdev.agent.infrastructure.orcid.port.out.repository.OrcidProfileRepository
import pl.ejdev.agent.infrastructure.pubmed.port.out.search.articles.SearchArticlesPort
import pl.ejdev.agent.infrastructure.pubmed.service.PubmedRestClient
import pl.ejdev.agent.infrastructure.pubmed.usecase.GetPubmedArticleAbstractUseCase
import pl.ejdev.agent.infrastructure.pubmed.usecase.SearchArticlesUseCase
import pl.ejdev.agent.infrastructure.pubmed.usecase.SearchSummarizeArticlesUseCase
import pl.ejdev.agent.infrastructure.orcid.usecase.UpdateOrcidUseCase
import pl.ejdev.agent.infrastructure.pubmed.PubmedArticlesHandler
import pl.ejdev.agent.utils.features

fun BeanDefinitionDsl.orcidBeans() {
    this.environment({ features.orcid }) {
        bean<OrcidProfileRepository> { OrcidProfileRepositoryImpl(ref()) }
        bean<UpdateOrcidPort> { UpdateOrcidAdapter(ref()) }
        bean<UpdateOrcidUseCase>()
        bean<OrcidHandler>()
    }
}