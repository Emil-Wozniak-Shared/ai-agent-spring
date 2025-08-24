package pl.ejdev.agent.routes

import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.servlet.function.router
import pl.ejdev.agent.config.RouterConfig
import pl.ejdev.agent.config.exceptions.ExceptionHandlerFilter
import pl.ejdev.agent.config.htmlRoutes
import pl.ejdev.agent.infrastructure.documents.DocumentHandler
import pl.ejdev.agent.infrastructure.user.UserHandler
import pl.ejdev.agent.security.jwt.TokenHandler

fun BeanDefinitionDsl.routes() {
    bean {
        val userHandler = ref<UserHandler>()
        val documentHandler = ref<DocumentHandler>()
        val tokenHandler = ref<TokenHandler>()

        router {
            filter(RouterConfig::filter)
            filter(ExceptionHandlerFilter::filter)
            "/api".nest {
                "/users".nest {
                    GET("", userHandler::getAllUsers)
                    GET("/{id}", userHandler::getUserById)
                    POST("", userHandler::createUser)
                    PUT("/{id}", userHandler::updateUser)
                }
                ("/documents" and accept(APPLICATION_JSON) and contentType(APPLICATION_JSON)).nest {
                    POST("", documentHandler::createMany)
                    POST("/search", documentHandler::search)
                }
                POST("/token", tokenHandler::create)
            }
            htmlRoutes()
        }
    }
}