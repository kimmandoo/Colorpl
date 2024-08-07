package com.data.datasource.remote

import com.data.model.request.RequestGoogleSignIn
import com.data.model.request.RequestSignIn
import com.data.model.response.ResponseSignIn
import com.data.model.response.ResponseSignUp
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface SignDataSource {

    suspend fun postSignIn(
        requestSignIn: RequestSignIn
    ): ResponseSignIn


    suspend fun postSignUp(
        requestSignUp: RequestBody,
        profileImage: MultipartBody.Part?
    ): ResponseSignUp


    suspend fun postGoogleSignIn(
        requestGoogleSignIn: RequestGoogleSignIn
    ): ResponseSignIn
}