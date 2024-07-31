package com.data.repositoryimpl

import com.data.api.safeApiCall
import com.data.datasource.local.TokenDataSource
import com.data.repository.TokenRepository
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenDataSource: TokenDataSource
) : TokenRepository {

    override suspend fun getAccessToken(): Flow<ApiResult<String>> = flow {
        emit(
            safeApiCall {
                tokenDataSource.getAccessToken()
            }
        )
    }

    override suspend fun setAccessToken(accessToken: String) {
        return tokenDataSource.setAccessToken(accessToken)
    }
}