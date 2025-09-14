package pl.ejdev.agent.infrastructure.orcid.adapter

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import pl.ejdev.agent.infrastructure.orcid.dao.OrcidProfileEntity
import pl.ejdev.agent.infrastructure.orcid.dao.OrcidProfileTable
import pl.ejdev.agent.infrastructure.orcid.port.out.repository.OrcidProfileRepository

class OrcidProfileRepositoryImpl(
    private val database: Database
) : OrcidProfileRepository {
    override fun find(email: String) = transaction(database) {
        OrcidProfileEntity
            .find { OrcidProfileTable.email eq email }
            .singleOrNull()
    }

    override fun update(id: String, email: String) = transaction(database) {
        OrcidProfileTable
            .update(
                where = { OrcidProfileTable.email eq email }
            ) {
                it[OrcidProfileTable.email] = email
                it[OrcidProfileTable.orcid] = id
            }
        return@transaction
    }
}