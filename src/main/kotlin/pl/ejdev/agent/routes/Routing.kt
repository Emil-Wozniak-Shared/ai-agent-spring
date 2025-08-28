package pl.ejdev.agent.routes

import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router
import pl.ejdev.agent.config.RouterConfig
import pl.ejdev.agent.config.exceptions.ExceptionHandlerFilter
import pl.ejdev.agent.config.htmlRoutes
import pl.ejdev.agent.infrastructure.documents.DocumentHandler
import pl.ejdev.agent.infrastructure.pubmed.PubmedArticlesHandler
import pl.ejdev.agent.infrastructure.user.UserHandler
import pl.ejdev.agent.security.jwt.TokenHandler

fun BeanDefinitionDsl.routes() {
    bean<RouterFunction<ServerResponse>> {
        routerFunction(
            userHandler = ref(),
            documentHandler = ref(),
            tokenHandler = ref(),
            pubmedArticlesHandler = ref()
        )
    }
}

fun routerFunction(
    userHandler: UserHandler,
    documentHandler: DocumentHandler,
    tokenHandler: TokenHandler,
    pubmedArticlesHandler: PubmedArticlesHandler
): RouterFunction<ServerResponse> = router {
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
        ("/pubmed" and contentType(APPLICATION_JSON)).nest {
            POST("/search/articles", pubmedArticlesHandler::search)
        }
        POST("/token", tokenHandler::create)
    }
    htmlRoutes()
}