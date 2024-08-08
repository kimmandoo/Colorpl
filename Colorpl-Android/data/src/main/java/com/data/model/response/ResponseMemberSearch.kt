package com.data.model.response

data class ResponseMemberSearch(
    val followerCount: Int,
    val followingCount: Int,
    val memberId: Int,
    val nickname: String,
    val profileImage: String,
    val reviewCount: Int
)
