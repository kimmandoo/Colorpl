package com.data.datasource.remote

import com.data.model.request.RequestSignIn
import com.data.model.response.ResponseSignIn
import kotlinx.coroutines.flow.Flow

interface SignDataSource {

    suspend fun postSignIn(
        requestSignIn: RequestSignIn
    ): ResponseSignIn

}