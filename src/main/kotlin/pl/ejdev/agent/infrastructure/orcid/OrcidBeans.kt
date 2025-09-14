package pl.ejdev.agent.infrastructure.orcid

import org.springframework.context.support.BeanDefinitionDsl
import pl.ejdev.agent.infrastructure.orcid.adapter.FindOrcidAdapter
import pl.ejdev.agent.infrastructure.orcid.adapter.OrcidProfileRepositoryImpl
import pl.ejdev.agent.infrastructure.orcid.adapter.UpdateOrcidAdapter
import pl.ejdev.agent.infrastructure.orcid.port.`in`.FindOrcidPort
import pl.ejdev.agent.infrastructure.orcid.port.`in`.UpdateOrcidPort
import pl.ejdev.agent.infrastructure.orcid.port.out.repository.OrcidProfileRepository
import pl.ejdev.agent.infrastructure.orcid.usecase.FindOrcidUseCase
import pl.ejdev.agent.infrastructure.orcid.usecase.UpdateOrcidUseCase
import pl.ejdev.agent.utils.features

fun BeanDefinitionDsl.orcidBeans() {
    this.environment({ features.orcid }) {
        bean<OrcidProfileRepository> { OrcidProfileRepositoryImpl(ref()) }
        bean<UpdateOrcidPort> { UpdateOrcidAdapter(ref()) }
        bean<FindOrcidPort> { FindOrcidAdapter(ref()) }
        bean<UpdateOrcidUseCase>()
        bean<FindOrcidUseCase>()
        bean<OrcidHandler>()
    }
}