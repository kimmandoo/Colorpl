package com.domain.mapper

import com.data.model.request.RequestSignIn
import com.data.model.response.ResponseSignIn
import com.domain.model.User

fun ResponseSignIn.toEntity(): User {
    return User(
        email = this.email,
        type = this.type,
        nickName = "",
        password = "",
        accessToken = this.accessToken,
        refreshToken = this.refreshToken
    )
}

fun User.toParam(): RequestSignIn {
    return RequestSignIn(
        email = this.email,
        password = this.password,
    )
}