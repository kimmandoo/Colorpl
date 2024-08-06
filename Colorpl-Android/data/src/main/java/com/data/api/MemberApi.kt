package com.data.api

import com.data.model.response.ResponseFollowCount
import com.data.model.response.ResponseMemberInfo
import retrofit2.http.GET

interface MemberApi {


    @GET("members")
    suspend fun getMemberInfo(): ResponseMemberInfo

    @GET("members/following/count")
    suspend fun getFollowingCount() : ResponseFollowCount

    @GET("members/followers/count")
    suspend fun getFollowersCount() : ResponseFollowCount

}