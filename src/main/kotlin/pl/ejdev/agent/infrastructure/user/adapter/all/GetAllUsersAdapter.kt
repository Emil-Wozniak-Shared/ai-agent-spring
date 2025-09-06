package pl.ejdev.agent.infrastructure.user.adapter.all

import org.springframework.security.core.context.SecurityContextHolder
import pl.ejdev.agent.domain.Authority
import pl.ejdev.agent.infrastructure.user.dto.GetAllUsersEvent
import pl.ejdev.agent.infrastructure.user.dto.GetAllUsersResult
import pl.ejdev.agent.infrastructure.user.port.`in`.GetAllUsersPort
import pl.ejdev.agent.infrastructure.user.port.out.UserRepository

class GetAllUsersAdapter(
    private val userRepository: UserRepository,
) : GetAllUsersPort {
    override fun handle(event: GetAllUsersEvent): GetAllUsersResult {
        val context = SecurityContextHolder.getContext()
        val authentication = context.authentication
        val isAdmin = authentication.authorities.any { it == Authority.ADMIN }
        val data =
            if (isAdmin)  userRepository.findAll()
            else listOf(userRepository.findByName(authentication.name)!!)
        return GetAllUsersResult(data)
    }
}