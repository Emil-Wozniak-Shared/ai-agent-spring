package pl.ejdev.agent.infrastructure.documents

import org.springframework.context.support.BeanDefinitionDsl
import pl.ejdev.agent.infrastructure.documents.adapter.create.CreateDocumentAdapter
import pl.ejdev.agent.infrastructure.documents.adapter.search.SearchDocumentAdapter
import pl.ejdev.agent.infrastructure.documents.usecase.create.CreateDocumentUseCase
import pl.ejdev.agent.infrastructure.documents.usecase.search.SearchDocumentUseCase

fun BeanDefinitionDsl.documentBeans() {
    bean<SearchDocumentUseCase> { SearchDocumentAdapter(ref()) }
    bean<CreateDocumentUseCase> { CreateDocumentAdapter( ref()) }
    bean<DocumentHandler>()
}