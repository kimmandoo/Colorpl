package com.data.datasourceimpl

import com.data.api.SignApi
import com.data.datasource.remote.SignDataSource
import com.data.model.request.RequestGoogleSignIn
import com.data.model.request.RequestSignIn
import com.data.model.request.RequestSignUp
import com.data.model.response.ResponseSignIn
import com.data.model.response.ResponseSignUp
import javax.inject.Inject

class SignDataSourceImpl @Inject constructor(
    private val signApi: SignApi
) : SignDataSource {

    override suspend fun postSignIn(requestSignIn: RequestSignIn): ResponseSignIn {
        return signApi.postSignIn(requestSignIn)

    }


    override suspend fun postSignUp(requestSignUp: RequestSignUp): ResponseSignUp {
        return signApi.postSignUp(requestSignUp)
    }

    override suspend fun postGoogleSignIn(requestGoogleSignIn: RequestGoogleSignIn): ResponseSignIn {
        return signApi.postGoogleSignIn(requestGoogleSignIn)
    }
}