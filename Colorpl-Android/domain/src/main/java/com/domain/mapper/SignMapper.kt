package com.domain.mapper

import com.data.model.request.RequestSignIn
import com.data.model.request.RequestSignUp
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

fun User.toSignInParam(): RequestSignIn {
    return RequestSignIn(
        email = this.email,
        password = this.password,
    )
}

fun User.toSignUpParam(): RequestSignUp {
    return RequestSignUp(
        email = this.email,
        password = this.password,
        nickname = this.nickName,
        profile = this.profileImage
    )
}