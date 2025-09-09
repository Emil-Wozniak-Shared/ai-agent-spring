package pl.ejdev.agent.infrastructure.orcid.dao

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import pl.ejdev.agent.infrastructure.user.dao.User

class OrcidProfile(id: EntityID<Long>) : Entity<Long>(id) {
    companion object : EntityClass<Long, OrcidProfile>(table = OrcidProfileTable, OrcidProfile::class.java)

    var user by User.Companion optionalReferencedOn OrcidProfileTable.userId
    var email by OrcidProfileTable.email
    var orcid by OrcidProfileTable.orcid
}