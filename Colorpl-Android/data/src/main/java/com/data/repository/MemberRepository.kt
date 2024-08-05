package com.data.repository

import com.data.model.response.ResponseMemberInfo
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow


interface MemberRepository {


    suspend fun getMemberInfo(): Flow<ApiResult<ResponseMemberInfo>>
}