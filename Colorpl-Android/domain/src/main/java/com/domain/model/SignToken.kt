package com.domain.model

data class SignToken(
    val email: String,
    val password: String,
    val accessToken: String,
    val refreshToken: String,
    val loginType : Boolean
)