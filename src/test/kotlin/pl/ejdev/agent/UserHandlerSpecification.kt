package pl.ejdev.agent

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.get
import kotlin.test.Test

class UserHandlerSpecification : BaseMvcTest() {

    @Test
    fun `should return all mocked users`() = runTestApplication( "test") {
        get("/api/users")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(APPLICATION_JSON) }
                jsonPath("$.length()") { value(2) }
            }
    }

    @Test
    fun `should return first mocked users`() = runTestApplication("test") {
        get("/api/users/1")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(APPLICATION_JSON) }
                jsonPath("$.id") { value(1) }
                jsonPath("$.name") { value("admin") }
                jsonPath("$.roles") { value("ADMIN") }
            }
    }
}