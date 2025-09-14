package pl.ejdev.agent.utils

import org.springframework.web.servlet.function.ServerRequest

val ServerRequest.id: Long?
    get() = pathVariable("id").toLongOrNull()
