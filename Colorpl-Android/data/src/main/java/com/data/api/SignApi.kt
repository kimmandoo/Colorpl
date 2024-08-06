package com.data.api

import com.data.model.request.RequestGoogleSignIn
import com.data.model.request.RequestSignIn
import com.data.model.response.ResponseSignIn
import com.data.model.response.ResponseSignUp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SignApi {

    @POST("members/sign-in")
    suspend fun postSignIn(
        @Body requestSignIn: RequestSignIn
    ): ResponseSignIn

    @Multipart
    @POST("members/register")
    suspend fun postSignUp(
        @Part("memberDTO") memberDTO: RequestBody,
        @Part profileImage: MultipartBody.Part?
    ): ResponseSignUp

    @POST("oauth-sign-in")
    suspend fun postGoogleSignIn(
        @Body requestGoogleSignIn: RequestGoogleSignIn
    ): ResponseSignIn

}
