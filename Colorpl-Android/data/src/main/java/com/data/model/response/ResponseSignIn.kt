package com.data.model.response

data class ResponseSignIn(
    val email: String,
    val type: String,
    val accessToken: String,
    val refreshToken: String
)
