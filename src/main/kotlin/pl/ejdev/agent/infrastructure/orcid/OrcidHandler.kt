package pl.ejdev.agent.infrastructure.orcid

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import pl.ejdev.agent.infrastructure.orcid.dto.UpdateOrcidQuery
import pl.ejdev.agent.infrastructure.orcid.dto.UpdateOrcidRequest
import pl.ejdev.agent.infrastructure.orcid.dto.UpdateOrcidResult
import pl.ejdev.agent.infrastructure.orcid.usecase.UpdateOrcidUseCase

class OrcidHandler(
    private val updateOrcidUseCase: UpdateOrcidUseCase
) {

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
