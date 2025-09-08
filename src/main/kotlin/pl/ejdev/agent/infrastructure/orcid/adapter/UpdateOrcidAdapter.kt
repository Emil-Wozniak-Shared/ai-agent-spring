package pl.ejdev.agent.infrastructure.orcid.adapter

import pl.ejdev.agent.infrastructure.orcid.dto.UpdateOrcidEvent
import pl.ejdev.agent.infrastructure.orcid.dto.UpdateOrcidResult
import pl.ejdev.agent.infrastructure.orcid.port.`in`.UpdateOrcidPort
import pl.ejdev.agent.infrastructure.orcid.port.out.repository.OrcidProfileRepository

class UpdateOrcidAdapter(
    private val pubmedProfileRepository: OrcidProfileRepository
) : UpdateOrcidPort {
    override fun handle(event: UpdateOrcidEvent): UpdateOrcidResult = event
        .runCatching { pubmedProfileRepository.update(id, email) }
        .map { UpdateOrcidResult.Success }
        .getOrElse { UpdateOrcidResult.Failure("${it.message}") }
}