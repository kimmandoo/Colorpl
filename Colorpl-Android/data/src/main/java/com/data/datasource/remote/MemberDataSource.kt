package com.data.datasource.remote

import com.data.model.response.ResponseFollowCount
import com.data.model.response.ResponseMemberInfo
import com.data.model.response.ResponseMemberSearch
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface MemberDataSource {

    suspend fun getMemberInfo(): ResponseMemberInfo

    suspend fun getFollowingCount(): ResponseFollowCount

    suspend fun getFollowerCount(): ResponseFollowCount

    suspend fun updateMemberInfo(
        requestMemberInfo: RequestBody?,
        profileImage: MultipartBody.Part?
    ): ResponseMemberInfo

    suspend fun getMemberSearch(nickname: String): List<ResponseMemberSearch>

    suspend fun getOtherMember(memberId: Int): ResponseMemberInfo

    suspend fun getOtherFollowingCount(memberId: Int): ResponseFollowCount

    suspend fun getOtherFollowersCount(memberId: Int): ResponseFollowCount

    suspend fun postFollow(followId : Int) : String

    suspend fun postUnFollow(followId : Int) : String

}