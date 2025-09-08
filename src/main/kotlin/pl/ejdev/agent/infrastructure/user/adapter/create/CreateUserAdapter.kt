package pl.ejdev.agent.infrastructure.user.adapter.create

import org.springframework.security.crypto.password.PasswordEncoder
import pl.ejdev.agent.infrastructure.user.dto.CreateUserEvent
import pl.ejdev.agent.infrastructure.user.dto.CreateUserResult
import pl.ejdev.agent.infrastructure.user.port.`in`.CreateUserPort
import pl.ejdev.agent.infrastructure.user.port.out.UserRepository

class CreateUserAdapter(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : CreateUserPort {
    override fun handle(event: CreateUserEvent): CreateUserResult =
        userRepository
            .runCatching {
                val user = event.userDto
                user.copy(password = passwordEncoder.encode(user.password)).let(this::save)
            }
            .map {
                if (it == -1L) CreateUserResult.Failure("User already exists")
                else CreateUserResult.Success(it)
            }
            .getOrElse { CreateUserResult.Failure("${it.message}") }
}