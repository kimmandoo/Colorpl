package com.domain.model

data class User(
    val email: String,
    val password : String,
    val nickName : String = "",
    val type : String ="",
    val profile : String ="",
    val accessToken: String ="",
    val refreshToken: String =""
)
