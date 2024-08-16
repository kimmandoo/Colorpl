package com.data.model.response


data class ResponseVision(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val usage: Usage,
    val choices: List<Choice>
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)

data class Choice(
    val message: MessageResponse,
    val finish_details: FinishDetails,
    val index: Int
)

data class MessageResponse(
    val role: String,
    val content: String
)

data class FinishDetails(
    val type: String,
    val stop: String
)