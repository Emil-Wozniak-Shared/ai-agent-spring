package pl.ejdev.agent.infrastructure.user.get

import org.springframework.test.web.servlet.get
import pl.ejdev.agent.infrastructure.BaseMvcTest
import pl.ejdev.agent.infrastructure.applicationJson
import kotlin.test.Test

class GetUserMvcSpecification : BaseMvcTest() {
    @Test
    fun `should return first mocked users`() = runTestApplication {
        get("/api/users/1")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { applicationJson() }
                jsonPath("$.id") { value(1) }
                jsonPath("$.name") { value("admin") }
                jsonPath("$.roles") { value("ADMIN") }
            }
    }
}