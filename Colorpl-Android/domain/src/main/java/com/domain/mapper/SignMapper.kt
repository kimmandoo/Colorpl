package com.domain.mapper

import com.data.model.request.RequestSignIn
import com.data.model.request.RequestSignToken
import com.data.model.request.RequestSignUp
import com.data.model.response.ResponseSignIn
import com.data.model.response.ResponseSignToken
import com.domain.model.Member
import com.domain.model.SignToken

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

fun ResponseSignIn.toSignTokenParam(password: String = "", idToken: String = ""): RequestSignToken {
    return RequestSignToken(
        email = this.email,
        password = password,
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        idToken = idToken
    )
}

fun ResponseSignToken.toEntity(): SignToken {
    return SignToken(
        email = this.email,
        password = this.password,
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        idToken = this.idToken
    )
}


fun Member.toSignTokenParam(): RequestSignToken {
    return RequestSignToken(
        email = this.email,
        password = this.password,
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        idToken = this.idToken
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
