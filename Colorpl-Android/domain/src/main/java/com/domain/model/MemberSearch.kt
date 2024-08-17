package com.domain.model

import java.io.Serializable

data class MemberSearch(
    val followerCount: Int,
    val followingCount: Int,
    val memberId: Int,
    val nickname: String,
    val profileImage: String ?= "",
    val reviewCount: Int,
    val isFollowing : Boolean
) : Serializable
