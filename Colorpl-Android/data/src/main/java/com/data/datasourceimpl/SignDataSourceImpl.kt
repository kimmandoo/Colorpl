package com.data.datasourceimpl

import com.data.api.SignApi
import com.data.datasource.remote.SignDataSource
import com.data.model.request.RequestSignIn
import com.data.model.response.ResponseSignIn
import javax.inject.Inject

class SignDataSourceImpl @Inject constructor(
    private val signApi: SignApi
) : SignDataSource {

    override suspend fun postSignIn(requestSignIn: RequestSignIn): ResponseSignIn {
        return signApi.postSignIn(requestSignIn)

    }
}