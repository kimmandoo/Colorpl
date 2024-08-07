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

fun ResponseSignToken.toEntity(): SignToken {
    return SignToken(
        email = this.email,
        password = this.password,
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        idToken = this.idToken
    )
}

/**
 * loginType -> true : 일반, false : 구글 로그인
 */

fun String.toSignTokenParam() : RequestSignToken{
    return RequestSignToken(
        idToken = this
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
