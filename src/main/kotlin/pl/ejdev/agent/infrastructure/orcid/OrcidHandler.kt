package pl.ejdev.agent.infrastructure.orcid

import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import pl.ejdev.agent.infrastructure.orcid.dto.*
import pl.ejdev.agent.infrastructure.orcid.usecase.FindOrcidUseCase
import pl.ejdev.agent.infrastructure.orcid.usecase.UpdateOrcidUseCase

class OrcidHandler(
    private val findOrcidUseCase: FindOrcidUseCase,
    private val updateOrcidUseCase: UpdateOrcidUseCase,
) {

    fun find(request: ServerRequest): ServerResponse =
        findOrcidUseCase.handle(FindOrcidQuery)
            .let {
                when (it) {
                    is FindOrcidResult.Success -> ServerResponse.ok().contentType(APPLICATION_JSON).body(it)
                    is FindOrcidResult.Failure -> ServerResponse.badRequest().body(it.message)
                }
            }

    fun update(request: ServerRequest): ServerResponse =
        request.body<UpdateOrcidRequest>()
            .let { updateOrcidUseCase.handle(UpdateOrcidQuery(it.id)) }
            .let {
                when (it) {
                    is UpdateOrcidResult.Failure -> ServerResponse.badRequest().body(it.message)
                    is UpdateOrcidResult.Success -> ServerResponse.noContent().build()
                }
            }
}
