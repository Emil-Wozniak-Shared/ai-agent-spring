package pl.ejdev.agent.config

import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.exists
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.postgresql.Driver
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.core.env.get
import pl.ejdev.agent.infrastructure.orcid.dao.OrcidProfileTable
import pl.ejdev.agent.infrastructure.user.dao.UserTable

fun BeanDefinitionDsl.dbConfiguration() {
    bean<Database> {
        Database.connect(
            url = env["spring.datasource.url"]!!,
            user = env["spring.datasource.username"]!!,
            password = env["spring.datasource.password"]!!,
            driver = Driver::class.java.name
        ).apply {
            transaction {
                addLogger(StdOutSqlLogger)
                UserTable.createIfNotExist()
                OrcidProfileTable.createIfNotExist()
            }
        }
    }
}

private fun Table.createIfNotExist() {
    if (!this.exists()) {
        SchemaUtils.create(this)
    }
}
