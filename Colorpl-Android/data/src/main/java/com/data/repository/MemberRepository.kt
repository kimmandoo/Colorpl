package com.data.repository

import com.data.model.request.RequestMemberInfo
import com.data.model.response.ResponseFollowCount
import com.data.model.response.ResponseMemberInfo
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow
import java.io.File


interface MemberRepository {


    suspend fun getMemberInfo(): Flow<ApiResult<ResponseMemberInfo>>

    suspend fun getFollowingCount(): Flow<ApiResult<ResponseFollowCount>>

    suspend fun getFollowerCount(): Flow<ApiResult<ResponseFollowCount>>

    suspend fun updateMemberInfo(requestMemberInfo: RequestMemberInfo?, file : File?) : Flow<ApiResult<ResponseMemberInfo>>
}