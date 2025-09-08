package pl.ejdev.agent.domain

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import pl.ejdev.agent.infrastructure.orcid.dao.OrcidProfileTable
import pl.ejdev.agent.infrastructure.user.dao.UserTable
import pl.ejdev.agent.infrastructure.user.dto.CreateUserRequest
import java.time.LocalDateTime

enum class Authority : GrantedAuthority {
    USER {
        override fun getAuthority(): String = this::name.name
    },
    ADMIN {
        override fun getAuthority(): String = this::name.name
    };

    companion object {
        fun from(authority: GrantedAuthority) =
            when (authority.authority.substringAfter("_")) {
                "USER" -> USER
                "ADMIN" -> ADMIN
                else -> error("Unknown authority $authority")
            }

        fun from(authority: String) =
            when (authority) {
                "USER" -> USER
                "ADMIN" -> ADMIN
                else -> error("Unknown authority $authority")
            }
    }
}

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

class User(id: EntityID<Long>) : Entity<Long>(id), UserDetails {
    companion object : EntityClass<Long, User>(table = UserTable, entityType = User::class.java)

    var email by UserTable.email
    var name by UserTable.name

    @get:JvmName("rawPassword")
    var password by UserTable.password
    var active by UserTable.active
    var createdAt by UserTable.createdAt
    var updatedAt by UserTable.updatedAt
    var roles by UserTable.roles

    // One-to-one relationship - corrected reference name
    val orcidProfile by OrcidProfile optionalBackReferencedOn OrcidProfileTable.userId

    override fun getAuthorities(): Collection<GrantedAuthority> = roles.map { SimpleGrantedAuthority("ROLE_$it") }
    override fun getPassword(): String = password
    override fun getUsername(): String = name
}

class OrcidProfile(id: EntityID<Long>) : Entity<Long>(id) {
    companion object : EntityClass<Long, OrcidProfile>(table = OrcidProfileTable, OrcidProfile::class.java)

    var user by User optionalReferencedOn OrcidProfileTable.userId
    var email by OrcidProfileTable.email
    var orcid by OrcidProfileTable.orcid
}