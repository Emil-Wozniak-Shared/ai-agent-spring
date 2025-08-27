package pl.ejdev.agent.infrastructure

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.mockk
import io.qdrant.client.QdrantClient
import io.qdrant.client.QdrantGrpcClient
import jakarta.servlet.ServletContext
import org.springframework.ai.openai.OpenAiEmbeddingModel
import org.springframework.ai.openai.api.OpenAiApi
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.getBean
import org.springframework.beans.factory.support.DefaultListableBeanFactory
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans
import org.springframework.core.env.get
import org.springframework.mock.web.MockServletContext
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.MockMvcHttpConnector
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import pl.ejdev.agent.AgentApplication
import pl.ejdev.agent.config.AppBeansConfig.security
import pl.ejdev.agent.infrastructure.documents.documentBeans
import pl.ejdev.agent.infrastructure.embedding.embeddingBeans
import pl.ejdev.agent.infrastructure.qdrant.adapter.create.CreateQdrantAdapter
import pl.ejdev.agent.infrastructure.qdrant.adapter.search.SearchQdrantAdapter
import pl.ejdev.agent.infrastructure.qdrant.port.out.CreateQdrantPort
import pl.ejdev.agent.infrastructure.qdrant.port.out.SearchQdrantPort
import pl.ejdev.agent.infrastructure.qdrant.usecase.create.CreateQdrantUseCase
import pl.ejdev.agent.infrastructure.qdrant.usecase.search.SearchQdrantUseCase
import pl.ejdev.agent.infrastructure.user.userBeans
import pl.ejdev.agent.routes.routes
import kotlin.random.Random

abstract class BaseMvcTest {

    val openAiApi: OpenAiApi = mockk()
    val openAiEmbeddingModel: OpenAiEmbeddingModel = mockk()
    val vectorStore: VectorStore = mockk()
    val qdrantClient: QdrantClient = mockk()
    val qdrantGrpcClient: QdrantGrpcClient = mockk()

    fun runTestApplication(
        vararg profiles: String = arrayOf("test"),
        randomPort: Boolean = true,
        verification: MockMvc.() -> Unit
    ): Unit =
        runApplication<AgentApplication> {
            setAdditionalProfiles(*profiles)
            addInitializers(beansDsl(randomPort))
        }.run {
            getBean<MockMvc>().apply { verification() }
        }

    private fun beansDsl(randomPort: Boolean): BeanDefinitionDsl = beans {
        webServer(randomPort)
        security()
        openAiBeans()
        qdrantBeans()
        documentBeans()
        userBeans()
        embeddingBeans()
        routes()
    }

    private fun BeanDefinitionDsl.qdrantBeans() {
        bean<QdrantGrpcClient> { qdrantGrpcClient }
        bean<QdrantClient> { qdrantClient }
        bean<VectorStore> { vectorStore }
        bean<SearchQdrantPort> { SearchQdrantAdapter(ref()) }
        bean<CreateQdrantPort> { CreateQdrantAdapter(ref()) }
        bean<CreateQdrantUseCase> { CreateQdrantUseCase(ref()) }
        bean<SearchQdrantUseCase> { SearchQdrantUseCase(ref()) }
    }

    private fun BeanDefinitionDsl.openAiBeans() {
        bean<OpenAiApi> { openAiApi }
        bean<OpenAiEmbeddingModel> { openAiEmbeddingModel }
    }

    private fun BeanDefinitionDsl.webServer(randomPort: Boolean) {
        val port = getPort(randomPort)
        bean<ServletWebServerFactoryAutoConfiguration.BeanPostProcessorsRegistrar>()
        bean<DispatcherServletAutoConfiguration>()
        bean<WebMvcAutoConfiguration>()
        bean<DefaultListableBeanFactory>()
        bean<ServletContext> { MockServletContext() }
        bean<MockMvcAutoConfiguration>()
        bean { jacksonObjectMapper {} }
        bean { TomcatServletWebServerFactory(port) }
        bean { MockMvcHttpConnector(ref()) }
        bean { MockMvcBuilders.routerFunctions(ref()).build() }
        bean { WebTestClient.bindToServer(ref()).build() }
    }

    private fun BeanDefinitionDsl.getPort(randomPort: Boolean): Int = if (randomPort) Random.Default.nextInt(8080, 8442)
    else if (env["server.port"] != null) env["server.port"]!!.toInt()
    else 8080
}