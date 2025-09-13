package pl.ejdev.agent.domain

import pl.ejdev.agent.infrastructure.user.dao.UserEntity
import pl.ejdev.agent.infrastructure.user.dto.CreateUserRequest
import java.time.LocalDateTime

data class UserDto(
    val name: String,
    val firstName: String,
    val lastName: String,
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
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            password = request.password,
            roles = listOf(Authority.USER)
        )

        fun from(entity: UserEntity) = entity.run {
            UserDto(
                name,
                firstName,
                lastName,
                email,
                password = "****",
                roles = roles.map { Authority.from(it) }
            )
        }
    }
}

