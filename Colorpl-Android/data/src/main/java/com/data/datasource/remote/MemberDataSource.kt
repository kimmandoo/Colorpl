package com.data.datasource.remote

import com.data.model.response.ResponseMemberInfo

interface MemberDataSource {

    suspend fun getMemberInfo(): ResponseMemberInfo
}