package com.data.model.response

data class ResponseSignToken(
    val email: String,
    val password: String,
    val accessToken: String,
    val refreshToken: String,
    val loginType : Boolean
)