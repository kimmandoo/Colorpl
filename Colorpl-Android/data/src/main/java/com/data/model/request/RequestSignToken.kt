package com.data.model.request

data class RequestSignToken(
    val email: String = "",
    val password: String = "",
    val accessToken: String = "",
    val refreshToken: String = "",
    val idToken : String = ""
)