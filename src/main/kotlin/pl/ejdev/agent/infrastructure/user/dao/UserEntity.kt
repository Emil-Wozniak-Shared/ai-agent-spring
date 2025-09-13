package pl.ejdev.agent.infrastructure.user.dao

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import pl.ejdev.agent.infrastructure.orcid.dao.OrcidProfile
import pl.ejdev.agent.infrastructure.orcid.dao.OrcidProfileTable

class UserEntity(id: EntityID<Long>) : Entity<Long>(id), UserDetails {
    companion object : EntityClass<Long, UserEntity>(table = UserTable, entityType = UserEntity::class.java)

    var email by UserTable.email
    var name by UserTable.name
    var firstName by UserTable.firstName
    var lastName by UserTable.lastName

    @get:JvmName("rawPassword")
    var password by UserTable.password
    var active by UserTable.active
    var createdAt by UserTable.createdAt
    var updatedAt by UserTable.updatedAt
    var roles by UserTable.roles

    // One-to-one relationship - corrected reference name
    val orcidProfile by OrcidProfile.Companion optionalBackReferencedOn OrcidProfileTable.userId

    override fun getAuthorities(): Collection<GrantedAuthority> = roles.map { SimpleGrantedAuthority("ROLE_$it") }
    override fun getPassword(): String = password
    override fun getUsername(): String = name
}