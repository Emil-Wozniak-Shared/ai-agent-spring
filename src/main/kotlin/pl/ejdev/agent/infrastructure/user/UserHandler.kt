package pl.ejdev.agent.infrastructure.user

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import pl.ejdev.agent.domain.Authority
import pl.ejdev.agent.domain.User
import pl.ejdev.agent.infrastructure.user.dto.CreateUserRequest
import pl.ejdev.agent.infrastructure.user.dto.UpdateUserRequest

class UserHandler(private val userService: UserService) {

    fun getAllUsers(request: ServerRequest): ServerResponse {
        return try {
            val users = userService.findAll()
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(users)
        } catch (e: Exception) {
            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "Failed to retrieve users"))
        }
    }

    fun getUserById(request: ServerRequest): ServerResponse {
        return try {
            val id = request.pathVariable("id").toLongOrNull()
                ?: return ServerResponse.badRequest()
                    .body(mapOf("error" to "Invalid user ID"))

            val user = userService.findById(id)
            if (user != null) {
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(user)
            } else {
                ServerResponse.notFound().build()
            }
        } catch (e: Exception) {
            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "Failed to retrieve user"))
        }
    }

    fun createUser(request: ServerRequest): ServerResponse {
        return try {
            val userRequest = request.body<CreateUserRequest>()

            // Validation
            if (userRequest.password.isBlank() || userRequest.name.isBlank()) {
                return ServerResponse.badRequest()
                    .body(mapOf("error" to "Name and password are required"))
            }

            val user = User(
                name = userRequest.name,
                hashPassword = userRequest.password,
                roles = listOf(Authority.USER)
            )

            val savedUser = userService.save(user)
            ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedUser)

        } catch (e: Exception) {
            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "Failed to create user"))
        }
    }

    fun updateUser(request: ServerRequest): ServerResponse {
        return try {
            val id = request.pathVariable("id").toLongOrNull()
                ?: return ServerResponse.badRequest()
                    .body(mapOf("error" to "Invalid user ID"))

            val userRequest = request.body<UpdateUserRequest>()
            val existingUser = userService.findById(id)
                ?: return ServerResponse.notFound().build()

            val updatedUser = existingUser.copy(
                name = userRequest.name ,
                hashPassword = userRequest.password
            )

            val savedUser = userService.save(updatedUser)
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedUser)

        } catch (e: Exception) {
            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "Failed to update user"))
        }
    }
}