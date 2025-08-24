package pl.ejdev.agent.config

import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpMethod
import org.springframework.web.servlet.function.RouterFunctionDsl

fun RouterFunctionDsl.htmlRoutes() {
    resource("/", "index")
    resource("/login", "login")
}

private fun RouterFunctionDsl.resource(path: String, name: String) =
    resource({ it.method() == HttpMethod.GET && it.path() == path },
        ClassPathResource("static/${name}.html")
    )