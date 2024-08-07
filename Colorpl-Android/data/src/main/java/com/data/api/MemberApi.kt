package com.data.api

import com.data.model.response.ResponseFollowCount
import com.data.model.response.ResponseMemberInfo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part

interface MemberApi {


    @GET("members")
    suspend fun getMemberInfo(): ResponseMemberInfo

    @GET("members/following/count")
    suspend fun getFollowingCount() : ResponseFollowCount

    @GET("members/followers/count")
    suspend fun getFollowersCount() : ResponseFollowCount

    @Multipart
    @PUT("members")
    suspend fun updateMemberInfo(
        @Part("memberDTO") memberDTO : RequestBody,
        @Part profileImage : MultipartBody.Part?
    ) : ResponseMemberInfo
}