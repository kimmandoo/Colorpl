package com.data.model.request

data class RequestSignUp(
    val email: String,
    val nickname: String,
    val password: String,
    val profile: String
)
