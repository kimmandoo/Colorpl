package com.data.repositoryimpl

import com.data.api.safeApiCall
import com.data.datasource.remote.MemberDataSource
import com.data.model.response.ResponseMemberInfo
import com.data.repository.MemberRepository
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val memberDataSource: MemberDataSource
) : MemberRepository {


    override suspend fun getMemberInfo(): Flow<ApiResult<ResponseMemberInfo>> {
        return flow {
            emit(safeApiCall {
                memberDataSource.getMemberInfo()
            })
        }
    }
}