package pl.ejdev.agent.infrastructure.pubmed.utils

private const val UNKNOWN = "N/A"

fun String?.orUnknown() = this ?: UNKNOWN