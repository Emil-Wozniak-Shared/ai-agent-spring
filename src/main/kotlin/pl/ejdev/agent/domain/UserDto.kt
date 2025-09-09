package pl.ejdev.agent.domain

import pl.ejdev.agent.infrastructure.user.dao.User
import pl.ejdev.agent.infrastructure.user.dto.CreateUserRequest
import java.time.LocalDateTime

data class UserDto(
    val name: String,
    val email: String,
    val password: String,
    val active: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val roles: List<Authority>
) {
    companion object {
        fun from(request: CreateUserRequest) = UserDto(
            name = request.name,
            email = request.email,
            password = request.password,
            roles = listOf(Authority.USER)
        )

        fun from(entity: User) = entity.run {
            UserDto(
                name = name,
                email = email,
                password = "****",
                roles = roles.map { Authority.from(it) }
            )
        }
    }
}

