package com.data.api

import com.data.model.request.RequestSignIn
import com.data.model.response.ResponseSignIn
import retrofit2.http.Body
import retrofit2.http.POST

interface SignApi {

    @POST("members/sign-in")
    suspend fun postSignIn(
        @Body requestSignIn: RequestSignIn
    ): ResponseSignIn

}