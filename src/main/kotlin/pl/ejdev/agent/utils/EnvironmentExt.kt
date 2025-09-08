package pl.ejdev.agent.utils

import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.get

private const val ORCID = "orcid"
private const val PUBMED = "pubmed"

data class Features(
    val orcid: Boolean = false,
    val pubmed: Boolean = false,
)

val ConfigurableEnvironment.features: Features
    get() = this["app.features"]
        ?.let {
            Features(
                orcid = it.contains(ORCID),
                pubmed = it.contains(PUBMED)
            )
        }
        ?: Features()
