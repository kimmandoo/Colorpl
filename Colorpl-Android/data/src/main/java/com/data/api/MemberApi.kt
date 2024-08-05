package com.data.api

import com.data.model.response.ResponseMemberInfo
import retrofit2.http.GET

interface MemberApi {


    @GET("members")
    suspend fun getMemberInfo(): ResponseMemberInfo


}