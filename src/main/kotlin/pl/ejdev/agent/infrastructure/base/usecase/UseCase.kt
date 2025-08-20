package pl.ejdev.agent.infrastructure.base.usecase

interface UseCase<QUERY, RESULT> {
    fun handle(query: QUERY): RESULT
}