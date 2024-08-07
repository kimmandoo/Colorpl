package com.domain.mapper

import com.data.model.request.RequestSignToken
import com.data.model.response.ResponseMemberInfo
import com.domain.model.Member

fun ResponseMemberInfo.toEntity(): Member {
    return Member(
        email = this.email,
        password = this.password,
        nickName = this.nickname,
        profileImage = this.profile
    )
}


