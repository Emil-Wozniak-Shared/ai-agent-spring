package pl.ejdev.agent.infrastructure.user.getAll

import org.springframework.test.web.servlet.get
import pl.ejdev.agent.infrastructure.BaseMvcTest
import pl.ejdev.agent.infrastructure.applicationJson
import kotlin.test.Test

class GetUserListMvcSpecification : BaseMvcTest() {
    @Test
    fun `should return all mocked users`() = runTestApplication {
        get("/api/users")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { applicationJson() }
                jsonPath("$.length()") { value(2) }
            }
    }
}