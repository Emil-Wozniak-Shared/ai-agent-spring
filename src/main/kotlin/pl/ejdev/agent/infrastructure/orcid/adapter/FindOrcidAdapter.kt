package pl.ejdev.agent.infrastructure.orcid.adapter

import pl.ejdev.agent.infrastructure.orcid.dto.FindOrcidEvent
import pl.ejdev.agent.infrastructure.orcid.dto.FindOrcidResult
import pl.ejdev.agent.infrastructure.orcid.port.`in`.FindOrcidPort
import pl.ejdev.agent.infrastructure.orcid.port.out.repository.OrcidProfileRepository

class FindOrcidAdapter(
    private val pubmedProfileRepository: OrcidProfileRepository
) : FindOrcidPort {
    override fun handle(event: FindOrcidEvent): FindOrcidResult = event
        .runCatching { pubmedProfileRepository.find( email) }
        .map {
            if (it != null) {
                FindOrcidResult.Success(it.email, it.orcid)
            } else {
                FindOrcidResult.Failure("Orcid for email ${event.email} not found")
            }
        }
        .getOrElse { FindOrcidResult.Failure("${it.message}") }
}