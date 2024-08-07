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
import com.data.util.FormDataConverterUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import timber.log.Timber
import java.io.File
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

    override suspend fun signUp(
        requestSignUp: RequestSignUp,
        file: File?
    ): Flow<ApiResult<ResponseSignUp>> {
        return flow {
            emit(safeApiCall {
                val requestPart = FormDataConverterUtil.getJsonRequestBody(requestSignUp)
                val filePart: MultipartBody.Part? =
                    FormDataConverterUtil.getNullableMultiPartBody("profileImage", file)

                signDataSource.postSignUp(
                    requestSignUp = requestPart,
                    profileImage = filePart
                )
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