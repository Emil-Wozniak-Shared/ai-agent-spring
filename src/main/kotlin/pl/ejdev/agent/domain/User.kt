package pl.ejdev.agent.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import pl.ejdev.agent.infrastructure.user.dto.CreateUserRequest
import java.time.LocalDateTime

enum class Authority: GrantedAuthority {
    USER {
        override fun getAuthority(): String = this::name.name
    },
    ADMIN {
        override fun getAuthority(): String = this::name.name
    };
    companion object {
        fun from(authority: GrantedAuthority) = when(authority.authority.substringAfter("_")) {
            "USER" -> USER
            "ADMIN" -> ADMIN
            else -> error("Unknown authority $authority")
        }

        fun from(authority: String) = when(authority) {
            "USER" -> USER
            "ADMIN" -> ADMIN
            else -> error("Unknown authority $authority")
        }
    }
}

data class User(
    val id: Long? = null,
    val name: String,
    val hashPassword: String,
    val active: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val roles: List<Authority>
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = roles
    override fun getPassword(): String = hashPassword
    override fun getUsername(): String = name

    companion object {
        fun from(request: CreateUserRequest) = User(
            name = request.name,
            hashPassword = request.password,
            roles = listOf(Authority.USER)
        )
    }
}
