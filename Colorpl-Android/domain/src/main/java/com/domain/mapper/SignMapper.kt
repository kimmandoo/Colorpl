package com.domain.mapper

import com.data.model.request.RequestSignIn
import com.data.model.request.RequestSignUp
import com.data.model.response.ResponseSignIn
import com.domain.model.Member

fun ResponseSignIn.toEntity(): Member {
    return Member(
        email = this.email,
        type = this.type,
        nickName = "",
        password = "",
        accessToken = this.accessToken,
        refreshToken = this.refreshToken
    )
}

fun Member.toSignInParam(): RequestSignIn {
    return RequestSignIn(
        email = this.email,
        password = this.password,
    )
}

fun Member.toSignUpParam(): RequestSignUp {
    return RequestSignUp(
        email = this.email,
        password = this.password,
        nickname = this.nickName ?: "",
        profile = this.profileImage ?: "1",
        categories = this.categories
    )
}
