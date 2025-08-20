package pl.ejdev.agent.infrastructure.qdrant.port.out

import pl.ejdev.agent.infrastructure.base.port.Port
import pl.ejdev.agent.infrastructure.qdrant.dto.CreateQdrantEvent
import pl.ejdev.agent.infrastructure.qdrant.dto.CreateQdrantResult

interface CreateQdrantPort : Port<CreateQdrantEvent, CreateQdrantResult>