package com.data.datasource.remote

import com.data.model.request.RequestSignIn
import com.data.model.request.RequestSignUp
import com.data.model.response.ResponseSignIn
import com.data.model.response.ResponseSignUp

interface SignDataSource {

    suspend fun postSignIn(
        requestSignIn: RequestSignIn
    ): ResponseSignIn


    suspend fun postSignUp(
        requestSignUp: RequestSignUp
    ): ResponseSignUp

}