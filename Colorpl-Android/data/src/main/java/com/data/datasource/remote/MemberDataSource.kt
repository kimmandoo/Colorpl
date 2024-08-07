package com.data.datasource.remote

import com.data.model.response.ResponseFollowCount
import com.data.model.response.ResponseMemberInfo

interface MemberDataSource {

    suspend fun getMemberInfo(): ResponseMemberInfo

    suspend fun getFollowingCount(): ResponseFollowCount

    suspend fun getFollowerCount(): ResponseFollowCount
    
}