package com.data.datasource.remote

import com.data.model.request.RequestMemberInfo
import com.data.model.response.ResponseFollowCount
import com.data.model.response.ResponseMemberInfo
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface MemberDataSource {

    suspend fun getMemberInfo(): ResponseMemberInfo

    suspend fun getFollowingCount(): ResponseFollowCount

    suspend fun getFollowerCount(): ResponseFollowCount

    suspend fun updateMemberInfo(requestMemberInfo: RequestBody?, profileImage : MultipartBody.Part?): ResponseMemberInfo
}