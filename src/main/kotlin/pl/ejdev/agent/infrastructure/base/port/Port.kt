package pl.ejdev.agent.infrastructure.base.port

interface Port<EVENT, RESULT> {
    fun handle(event: EVENT): RESULT
}