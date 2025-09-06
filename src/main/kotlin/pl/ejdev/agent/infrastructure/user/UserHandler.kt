package pl.ejdev.agent.infrastructure.user

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import pl.ejdev.agent.domain.User
import pl.ejdev.agent.infrastructure.user.dto.*
import pl.ejdev.agent.infrastructure.user.usecase.CreateUserUseCase
import pl.ejdev.agent.infrastructure.user.usecase.GetAllUsersUseCase
import pl.ejdev.agent.infrastructure.user.usecase.GetUserUseCase
import pl.ejdev.agent.utils.id

class UserHandler(
    private val getUserUseCase: GetUserUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val createUserUseCase: CreateUserUseCase,

    ) {
    fun getAllUsers(request: ServerRequest): ServerResponse = try {
        val users: GetAllUsersResult = getAllUsersUseCase.handle(GetAllUsersQuery)
        ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .body(users)
    } catch (e: Exception) {
        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(mapOf("error" to "Failed to retrieve users"))
    }

    fun getUserById(request: ServerRequest): ServerResponse {
        return try {
            val id = request.pathVariable("id").toLongOrNull()
                ?: return ServerResponse.badRequest()
                    .body(mapOf("error" to "Invalid user ID"))

            when (val result = getUserUseCase.handle(GetUserQuery(id))) {
                is GetUserResult.Empty -> ServerResponse.notFound().build()
                is GetUserResult.Some -> ServerResponse.ok()
                    .contentType(APPLICATION_JSON)
                    .body(result.user)
            }
        } catch (e: Exception) {
            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "Failed to retrieve user"))
        }
    }

    fun createUser(request: ServerRequest): ServerResponse {
        try {
            val userRequest = request.body<CreateUserRequest>()
            if (userRequest.password.isBlank() || userRequest.name.isBlank()) {
                return ServerResponse.badRequest()
                    .body(mapOf("error" to "Name and password are required"))
            }

            return when (val result = createUserUseCase.handle(CreateUserQuery(User.from(userRequest)))) {
                is CreateUserResult.Failure -> ServerResponse.badRequest()
                    .contentType(APPLICATION_JSON)
                    .body(result.message)
                is CreateUserResult.Success -> ServerResponse.status(HttpStatus.CREATED)
                    .contentType(APPLICATION_JSON)
                    .body(result.id)
            }
        } catch (e: Exception) {
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "Failed to create user"))
        }
    }
    fun updateUser(request: ServerRequest): ServerResponse = try {
        val id = request.id ?: return missingId()
        when (val result = getUserUseCase.handle(GetUserQuery(id))) {
            is GetUserResult.Empty -> ServerResponse.notFound().build()
            is GetUserResult.Some -> {
                val userRequest = request.body<UpdateUserRequest>()
                val updatedUser = result.user.copy(
                    name = userRequest.name,
                    hashPassword = userRequest.password
                )

                when (val result = createUserUseCase.handle(CreateUserQuery(updatedUser))) {
                    is CreateUserResult.Failure -> ServerResponse.badRequest()
                        .body(result.message)
                    is CreateUserResult.Success ->  ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(result.id)
                }
            }
        }
    } catch (e: Exception) {
        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(mapOf("error" to "Failed to update user"))
    }

    private fun missingId(): ServerResponse =
        ServerResponse.badRequest().body(mapOf("error" to "Missing id"))
}