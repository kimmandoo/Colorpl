package com.data.model.request


data class RequestVision(
    val max_tokens: Int,
    val messages: List<Message>,
    val model: String
)

data class Message(
    val content: List<Content>,
    val role: String
)

data class ImageUrl(
    val url: String
)

data class Content(
    val image_url: ImageUrl? = null,
    val text: String? = null,
    val type: String
)