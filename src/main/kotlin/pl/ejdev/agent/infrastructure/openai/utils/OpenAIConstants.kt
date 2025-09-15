package pl.ejdev.agent.infrastructure.openai.utils

object OpenAIConstants {
    const val TEMPERATURE = 0.7

    enum class Model(val text: String) {
        GPT_4("gpt-4")
    }
}