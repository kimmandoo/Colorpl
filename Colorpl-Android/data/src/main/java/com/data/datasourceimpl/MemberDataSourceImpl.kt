package com.data.datasourceimpl

import com.data.api.MemberApi
import com.data.datasource.remote.MemberDataSource
import com.data.model.response.ResponseFollowCount
import com.data.model.response.ResponseMemberInfo
import javax.inject.Inject

class MemberDataSourceImpl @Inject constructor(
    private val memberApi: MemberApi
) : MemberDataSource {

    override suspend fun getMemberInfo(): ResponseMemberInfo {
        return memberApi.getMemberInfo()
    }

    override suspend fun getFollowingCount(): ResponseFollowCount {
        return memberApi.getFollowingCount()
    }

    override suspend fun getFollowerCount(): ResponseFollowCount {
        return memberApi.getFollowersCount()
    }
}