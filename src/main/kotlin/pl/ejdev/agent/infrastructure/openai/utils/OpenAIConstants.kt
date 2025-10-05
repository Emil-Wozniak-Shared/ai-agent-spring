package pl.ejdev.agent.infrastructure.openai.utils

object OpenAIConstants {
    const val TEMPERATURE = 0.7

    enum class Model(val text: String) {
        GPT_3_5_TURBO("gpt-3.5-turbo"),
        GPT_4("gpt-4"),
    }
}