package pl.ejdev.agent.utils

import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriBuilder

fun RestClient.get(path: String, uri: UriBuilder.() -> Unit) = this.get().uri {
    it.path(path).apply { uri() }.build()
}

fun UriBuilder.queryParams(vararg param: Pair<String, Any>) = this.queryParams(params(*param))

fun params(vararg param: Pair<String, Any>): MultiValueMap<String, String> {
    val parts: MultiValueMap<String, String> = LinkedMultiValueMap()
    mapOf(*param).forEach { entry ->
        parts.add(entry.key, entry.value.toString())
    }
    return parts
}