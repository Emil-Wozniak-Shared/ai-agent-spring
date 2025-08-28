package pl.ejdev.agent.utils

import org.yaml.snakeyaml.util.UriEncoder

fun String.uriEncode(): String = UriEncoder.encode(this)