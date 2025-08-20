package pl.ejdev.agent.infrastructure.qdrant.port.out

import pl.ejdev.agent.infrastructure.base.port.Port
import pl.ejdev.agent.infrastructure.qdrant.dto.SearchQdrantEvent
import pl.ejdev.agent.infrastructure.qdrant.dto.SearchQdrantResult

interface SearchQdrantPort: Port<SearchQdrantEvent, SearchQdrantResult>