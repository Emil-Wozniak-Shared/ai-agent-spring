package pl.ejdev.agent.routes

import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router
import pl.ejdev.agent.config.exceptions.ExceptionHandlerFilter
import pl.ejdev.agent.config.web.RouterConfig
import pl.ejdev.agent.infrastructure.documents.DocumentHandler
import pl.ejdev.agent.infrastructure.openai.OpenAiHandler
import pl.ejdev.agent.infrastructure.orcid.OrcidHandler
import pl.ejdev.agent.infrastructure.pubmed.PubmedArticlesHandler
import pl.ejdev.agent.infrastructure.user.UserHandler
import pl.ejdev.agent.security.jwt.TokenHandler

fun BeanDefinitionDsl.routes() {
    bean<RouterFunction<ServerResponse>> {
        routerFunction(
            userHandler = ref(),
            documentHandler = ref(),
            tokenHandler = ref(),
            pubmedArticlesHandler = ref(),
            orcidHandler = ref(),
            openAiHandler = ref()
        )
    }
}

private const val UP = "UP"

fun routerFunction(
    userHandler: UserHandler,
    documentHandler: DocumentHandler,
    tokenHandler: TokenHandler,
    pubmedArticlesHandler: PubmedArticlesHandler,
    orcidHandler: OrcidHandler,
    openAiHandler: OpenAiHandler
): RouterFunction<ServerResponse> = router {
    filter(RouterConfig::filter)
    filter(ExceptionHandlerFilter::filter)
    ("/api" and contentType(APPLICATION_JSON)).nest {
        GET("/health") {
            ServerResponse.ok().contentType(APPLICATION_JSON).body(mapOf("status" to UP))
        }
        "/users".nest {
            GET("", userHandler::getAllUsers)
            GET("/{id}", userHandler::getUserByEmail)
            POST("", userHandler::createUser)
            PUT("/{id}", userHandler::updateUser)
        }
        "/documents".nest {
            POST("", documentHandler::createMany)
            POST("/search", documentHandler::search)
        }
        "/pubmed".nest {
            POST("/search/articles", pubmedArticlesHandler::search)
            POST("/search/articles/{ids}", pubmedArticlesHandler::searchBy)
            POST("/articles/{id}/abstract", pubmedArticlesHandler::abstract)
        }
        "/orcid".nest {
            GET(orcidHandler::find)
            PUT(orcidHandler::update)
        }
        "/ask".nest {
            GET("/describe", openAiHandler::describe)
        }
        "/token".nest {
            POST(tokenHandler::create)
        }
    }
}

