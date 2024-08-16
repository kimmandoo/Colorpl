package com.domain.usecaseimpl.member

import com.data.repository.MemberRepository
import com.data.util.ApiResult
import com.domain.mapper.toEntity
import com.domain.model.MemberSearch
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMemberSearchUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {


    suspend fun getMemberSearchInfo(nickname: String): Flow<DomainResult<List<MemberSearch>>> {
        return flow {
            memberRepository.getMemberSearch(nickname).collect { result ->
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