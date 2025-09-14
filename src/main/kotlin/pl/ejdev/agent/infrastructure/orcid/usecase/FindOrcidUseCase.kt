package pl.ejdev.agent.infrastructure.orcid.usecase

import org.springframework.security.core.context.SecurityContextHolder
import pl.ejdev.agent.infrastructure.user.dao.UserEntity
import pl.ejdev.agent.infrastructure.base.usecase.UseCase
import pl.ejdev.agent.infrastructure.orcid.dto.FindOrcidEvent
import pl.ejdev.agent.infrastructure.orcid.dto.FindOrcidQuery
import pl.ejdev.agent.infrastructure.orcid.dto.FindOrcidResult
import pl.ejdev.agent.infrastructure.orcid.port.`in`.FindOrcidPort

class FindOrcidUseCase(
    private val findOrcidPort: FindOrcidPort
) : UseCase<FindOrcidQuery, FindOrcidResult> {
    override fun handle(query: FindOrcidQuery): FindOrcidResult {
        val userEntityDto = SecurityContextHolder.getContext().authentication.principal as UserEntity
        return findOrcidPort.handle(FindOrcidEvent(userEntityDto.email))
    }
}