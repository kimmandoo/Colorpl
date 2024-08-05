package com.data.datasourceimpl

import com.data.api.MemberApi
import com.data.datasource.remote.MemberDataSource
import com.data.model.response.ResponseMemberInfo
import javax.inject.Inject

class MemberDataSourceImpl @Inject constructor(
    private val memberApi: MemberApi
) : MemberDataSource {

    override suspend fun getMemberInfo(): ResponseMemberInfo {
        return memberApi.getMemberInfo()
    }
}