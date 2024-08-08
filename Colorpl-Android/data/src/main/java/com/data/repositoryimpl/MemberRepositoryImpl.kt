package com.data.repositoryimpl

import com.data.api.safeApiCall
import com.data.datasource.remote.MemberDataSource
import com.data.model.request.RequestMemberInfo
import com.data.model.response.ResponseFollowCount
import com.data.model.response.ResponseMemberInfo
import com.data.repository.MemberRepository
import com.data.util.ApiResult
import com.data.util.FormDataConverterUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import java.io.File
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

    override suspend fun getFollowingCount(): Flow<ApiResult<ResponseFollowCount>> {
        return flow {
            emit(safeApiCall {
                memberDataSource.getFollowingCount()
            })
        }
    }

    override suspend fun getFollowerCount(): Flow<ApiResult<ResponseFollowCount>> {
        return flow {
            emit(safeApiCall {
                memberDataSource.getFollowerCount()
            })
        }
    }

    override suspend fun updateMemberInfo(
        requestMemberInfo: RequestMemberInfo?,
        file: File?
    ): Flow<ApiResult<ResponseMemberInfo>> {
        return flow {
            emit(safeApiCall {
                val requestPart = FormDataConverterUtil.getJsonRequestBody(requestMemberInfo)
                val filePart: MultipartBody.Part? =
                    FormDataConverterUtil.getNullableMultiPartBody("profileImage", file)

                memberDataSource.updateMemberInfo(
                    requestPart,
                    filePart
                )
            })
        }
    }
}