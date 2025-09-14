package pl.ejdev.agent.config

import org.jetbrains.exposed.v1.core.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.exists
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.migration.jdbc.MigrationUtils
import org.postgresql.Driver
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.core.env.get
import pl.ejdev.agent.infrastructure.orcid.dao.OrcidProfileTable
import pl.ejdev.agent.infrastructure.pubmed.dao.ArticleTable
import pl.ejdev.agent.infrastructure.user.dao.UserArticleTable
import pl.ejdev.agent.infrastructure.user.dao.UserTable

@OptIn(ExperimentalDatabaseMigrationApi::class)
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
                ArticleTable.createIfNotExist()
                UserArticleTable.createIfNotExist()
                MigrationUtils.statementsRequiredForDatabaseMigration(
                    UserTable, OrcidProfileTable, ArticleTable, UserArticleTable,
                    withLogs = true,
                )
                .forEach { exec(it) }
            }
        }
    }
}


private fun Table.createIfNotExist() {
    if (!this.exists()) {
        SchemaUtils.create(this)
    }
}
