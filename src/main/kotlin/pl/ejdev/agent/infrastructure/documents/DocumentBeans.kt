package pl.ejdev.agent.infrastructure.documents

import org.springframework.context.support.BeanDefinitionDsl
import pl.ejdev.agent.infrastructure.documents.usecase.create.CreateDocumentUseCase
import pl.ejdev.agent.infrastructure.documents.usecase.search.SearchDocumentUseCase

fun BeanDefinitionDsl.documentBeans() {
    bean<SearchDocumentUseCase> { SearchDocumentUseCase(ref(), ref()) }
    bean<CreateDocumentUseCase> { CreateDocumentUseCase(ref()) }
    bean<DocumentHandler>()
}