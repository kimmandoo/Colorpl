package com.data.repositoryimpl

import com.data.api.safeApiCall
import com.data.datasource.remote.SignDataSource
import com.data.model.request.RequestGoogleSignIn
import com.data.model.request.RequestSignIn
import com.data.model.request.RequestSignUp
import com.data.model.response.ResponseSignIn
import com.data.model.response.ResponseSignUp
import com.data.repository.SignRepository
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignRepositoryImpl @Inject constructor(
    private val signDataSource: SignDataSource
) : SignRepository {

    override suspend fun signIn(requestSignIn: RequestSignIn): Flow<ApiResult<ResponseSignIn>> {
        return flow {
            emit(safeApiCall {
                signDataSource.postSignIn(requestSignIn)
            })
        }
    }

    override suspend fun signUp(requestSignUp: RequestSignUp): Flow<ApiResult<ResponseSignUp>> {
        return flow {
            emit(safeApiCall {
                signDataSource.postSignUp(requestSignUp)
            })
        }
    }

    override suspend fun googleSignIn(requestGoogleSignIn: RequestGoogleSignIn): Flow<ApiResult<ResponseSignIn>> {
        return flow {
            emit(safeApiCall {
                signDataSource.postGoogleSignIn(requestGoogleSignIn)
            })
        }
    }
}