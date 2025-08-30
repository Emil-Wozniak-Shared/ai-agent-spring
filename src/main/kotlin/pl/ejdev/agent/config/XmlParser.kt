package pl.ejdev.agent.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlin.reflect.KClass

class XmlParser {

    private val kotlinXmlMapper = XmlMapper(JacksonXmlModule().apply {
        setDefaultUseWrapper(false)
    }).registerKotlinModule().apply {
        configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    internal fun <T : Any> parse(text: String, type: KClass<T>): T =
        kotlinXmlMapper.readValue(text, type.java)
}
