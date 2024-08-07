package com.data.datasource.local

import com.data.model.request.RequestSignToken
import com.data.model.response.ResponseSignToken

interface TokenDataSource {

    suspend fun setSignToken(requestSignToken: RequestSignToken)
    suspend fun getSignToken(): ResponseSignToken

    suspend fun getAccessToken(): String
    suspend fun setAccessToken(accessToken: String)
}