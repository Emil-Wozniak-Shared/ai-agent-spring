package pl.ejdev.agent.infrastructure.embedding.port.`in`

import pl.ejdev.agent.infrastructure.embedding.dto.GenerateEmbeddingQuery
import pl.ejdev.agent.infrastructure.embedding.dto.GenerateEmbeddingResult
import pl.ejdev.agent.infrastructure.base.usecase.UseCase

interface GenerateEmbeddingPort : UseCase<GenerateEmbeddingQuery, GenerateEmbeddingResult>