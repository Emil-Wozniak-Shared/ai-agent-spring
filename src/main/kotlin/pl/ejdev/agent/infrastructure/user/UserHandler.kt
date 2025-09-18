package pl.ejdev.agent.infrastructure.user

import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import pl.ejdev.agent.domain.UserDto
import pl.ejdev.agent.infrastructure.base.problem.problemDetails
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
    fun getAllUsers(request: ServerRequest): ServerResponse =
        ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .body(getAllUsersUseCase.handle(GetAllUsersQuery))

    fun getUserByEmail(request: ServerRequest): ServerResponse =
        request.id
            ?.let { id ->
                when (val result = getUserUseCase.handle(GetUserQuery(id))) {
                    is GetUserResult.Empty -> ServerResponse.notFound().build()
                    is GetUserResult.Some -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(result.userDto)
                }
            }
            ?: request.missingId()


    fun createUser(request: ServerRequest): ServerResponse = request.body<CreateUserRequest>()
        .takeIf { it.valid }
        ?.let {
            when (val result = createUserUseCase.handle(CreateUserQuery(UserDto.from(it)))) {
                is CreateUserResult.Success -> ServerResponse.status(CREATED)
                    .contentType(APPLICATION_JSON)
                    .body(result.id)

                is CreateUserResult.Failure -> problemDetails(request, "Failed to create user", result.message)
            }
        }
        ?: problemDetails(request, "Name and password are required")

    fun updateUser(request: ServerRequest): ServerResponse = request.id
        ?.let { id ->
            when (val getUserResult = getUserUseCase.handle(GetUserQuery(id))) {
                is GetUserResult.Empty -> ServerResponse.notFound().build()
                is GetUserResult.Some -> request.body<UpdateUserRequest>()
                    .let { getUserResult.userDto.copy(name = it.name, password = it.password) }
                    .let { updatedUser ->
                        when (val result = createUserUseCase.handle(CreateUserQuery(updatedUser))) {
                            is CreateUserResult.Failure -> ServerResponse.badRequest().body(result.message)

                            is CreateUserResult.Success -> ServerResponse.ok()
                                .contentType(APPLICATION_JSON)
                                .body(result.id)
                        }
                    }
            }
        }
        ?: request.missingId()

    private fun ServerRequest.missingId(): ServerResponse = problemDetails(this, "Invalid user ID")
}