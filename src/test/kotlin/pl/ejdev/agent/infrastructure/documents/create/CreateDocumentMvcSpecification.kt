package pl.ejdev.agent.infrastructure.documents.create

import io.mockk.justRun
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post
import pl.ejdev.agent.infrastructure.BaseMvcTest
import pl.ejdev.agent.infrastructure.applicationJson
import kotlin.test.Test

private const val EXPECTED_MESSAGE = "Documents added successfully"
private const val EXPECTED_COUNT = 1

class CreateDocumentMvcSpecification : BaseMvcTest() {
    @Test
    fun `should return first mocked users`() = runTestApplication {
        // given:
        justRun { vectorStore.add(any()) }

        // expect
        post("/api/documents") {
            contentType = MediaType.APPLICATION_JSON
            content = body
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { applicationJson() }
                jsonPath("$.message") { value(EXPECTED_MESSAGE) }
                jsonPath("$.count") { value(EXPECTED_COUNT) }
            }
    }

    private val body = """
                [{
                  "text": "This is a sample document about artificial intelligence and machine learning",
                  "metadata": {"category": "AI", "author": "John Doe", "tags": ["ai", "technology"]}
               }]
            """.trimIndent()
}