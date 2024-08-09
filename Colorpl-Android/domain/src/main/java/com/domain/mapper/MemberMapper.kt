package com.domain.mapper

import com.data.model.request.RequestMemberInfo
import com.data.model.response.ResponseMemberInfo
import com.data.model.response.ResponseMemberSearch
import com.domain.model.Member
import com.domain.model.MemberSearch

fun ResponseMemberInfo.toEntity(): Member {
    return Member(
        email = this.email,
        password = this.password,
        nickName = this.nickname,
        profileImage = this.profile
    )
}

fun String.toRequestMemberInfo(): RequestMemberInfo {
    return RequestMemberInfo(
        nickname = this
    )
}

fun List<ResponseMemberSearch>.toEntity(): List<MemberSearch> {
    return this.map { it ->
        MemberSearch(
            followerCount = it.followerCount,
            followingCount = it.followingCount,
            memberId = it.memberId,
            nickname = it.nickname,
            profileImage = it.profileImage,
            reviewCount = it.reviewCount,
            isFollowing = it.isFollowing
        )
    }
}


