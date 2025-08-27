package pl.ejdev.agent.infrastructure.qdrant.dto

sealed class CreateQdrantResult {
    class Success(
        val message: String = "Documents added successfully",
        val count: Int
    ): CreateQdrantResult()
    class Failure(
        val message: String
    ): CreateQdrantResult()
}