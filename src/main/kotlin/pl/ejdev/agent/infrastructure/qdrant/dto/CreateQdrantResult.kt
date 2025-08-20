package pl.ejdev.agent.infrastructure.qdrant.dto

sealed class CreateQdrantResult {
    class Success(val count: Int): CreateQdrantResult()
    class Failure(
        val message: String
    ): CreateQdrantResult()
}