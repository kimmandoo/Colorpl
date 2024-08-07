package com.data.model.response

data class ResponseMemberInfo(
    val email: String,
    val nickname: String,
    val password: String,
    val profile: String,
    val categories : List<String>
)
