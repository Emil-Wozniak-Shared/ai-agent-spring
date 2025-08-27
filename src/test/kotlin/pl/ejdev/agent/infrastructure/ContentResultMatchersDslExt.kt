package pl.ejdev.agent.infrastructure

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.result.ContentResultMatchersDsl

fun ContentResultMatchersDsl.applicationJson() =
    this.contentType(MediaType.APPLICATION_JSON)