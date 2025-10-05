package pl.ejdev.agent.utils

import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * @see java.sql.Connection
 */
enum class Isolation(val value: Int) {
    /**
     * @see java.sql.Connection.TRANSACTION_NONE
     */
    TRANSACTION_NONE(0),
    /**
     * @see java.sql.Connection.TRANSACTION_READ_UNCOMMITTED
     */
    TRANSACTION_READ_UNCOMMITTED(1),
    /**
     * @see java.sql.Connection.TRANSACTION_READ_COMMITTED
     */
    TRANSACTION_READ_COMMITTED(2),
    /**
     * @see java.sql.Connection.TRANSACTION_REPEATABLE_READ
     */
    TRANSACTION_REPEATABLE_READ(4),
    /**
     * @see java.sql.Connection.TRANSACTION_SERIALIZABLE
     */
    TRANSACTION_SERIALIZABLE(8)
}

inline fun <T> Database.read(
    isolation: Isolation = Isolation.TRANSACTION_READ_COMMITTED,
    crossinline statement: JdbcTransaction.() -> T,
): T = transaction(
    transactionIsolation = isolation.value,
    readOnly = true,
    db = this,
) {
    addLogger(StdOutSqlLogger)
    this@transaction.statement()
}

inline fun <T> Database.transaction(
    crossinline statement: JdbcTransaction.() -> T,
): T = transaction(db = this) {
    addLogger(StdOutSqlLogger)
     statement()
 }