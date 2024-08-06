package com.domain.model

data class Member(
    val email: String = "",
    val password: String = "",
    val nickName: String? = "",
    val type: String = "",
    val profileImage: String? = "",
    val accessToken: String = "",
    val refreshToken: String = "",
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val id: Int = 0,
    val categories: List<String> = emptyList()
)
