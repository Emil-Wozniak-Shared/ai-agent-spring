package pl.ejdev.agent.infrastructure.orcid.usecase

import org.springframework.security.core.context.SecurityContextHolder
import pl.ejdev.agent.infrastructure.user.dao.UserEntity
import pl.ejdev.agent.infrastructure.base.usecase.UseCase
import pl.ejdev.agent.infrastructure.orcid.dto.UpdateOrcidEvent
import pl.ejdev.agent.infrastructure.orcid.dto.UpdateOrcidQuery
import pl.ejdev.agent.infrastructure.orcid.dto.UpdateOrcidResult
import pl.ejdev.agent.infrastructure.orcid.port.`in`.UpdateOrcidPort

class UpdateOrcidUseCase(
    private val updateOrcidPort: UpdateOrcidPort
) : UseCase<UpdateOrcidQuery, UpdateOrcidResult> {
    override fun handle(query: UpdateOrcidQuery): UpdateOrcidResult {
        val userEntityDto = SecurityContextHolder.getContext().authentication.principal as UserEntity
        return updateOrcidPort.handle(UpdateOrcidEvent(query.id, userEntityDto.email))
    }
}