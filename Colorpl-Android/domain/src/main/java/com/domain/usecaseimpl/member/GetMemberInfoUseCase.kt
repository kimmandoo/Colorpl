package com.domain.usecaseimpl.member

import com.data.repository.MemberRepository
import com.data.util.ApiResult
import com.domain.mapper.toEntity
import com.domain.model.Member
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMemberInfoUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {

    suspend fun getMemberInfo(): Flow<DomainResult<Member>> {
        return flow {
            memberRepository.getMemberInfo().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        emit(DomainResult.success(result.data.toEntity()))
                    }
                    is ApiResult.Error -> {
                        emit(DomainResult.error(result.exception))
                    }
                }
            }
        }

    }
}