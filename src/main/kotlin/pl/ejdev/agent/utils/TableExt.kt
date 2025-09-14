package pl.ejdev.agent.utils

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.EnumerationNameColumnType
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.JavaLocalDateTimeColumnType
import pl.ejdev.agent.domain.Authority
import java.time.LocalDateTime

const val DEFAULT_VARCHAR_LENGTH = 255

inline fun <reified T: Enum<T>> Table.enumNameColumn(size: Int) =
    EnumerationNameColumnType(T::class, size)

inline fun <reified T: Enum<T>> Table.enumNameArray(name: String, size: Int): Column<List<Authority>> =
    array(name, enumNameColumn<Authority>( size))

fun Table.localDateTime(name: String): Column<LocalDateTime> =
    registerColumn(name, JavaLocalDateTimeColumnType())

