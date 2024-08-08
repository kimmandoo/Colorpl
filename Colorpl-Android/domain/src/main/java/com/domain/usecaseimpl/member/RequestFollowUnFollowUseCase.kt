package com.domain.usecaseimpl.member

import com.data.repository.MemberRepository
import com.data.util.ApiResult
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RequestFollowUnFollowUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {

    suspend fun follow(followerId: Int): Flow<DomainResult<String>> {
        return flow {
            memberRepository.postFollow(followerId).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        emit(DomainResult.Success(result.data))
                    }

                    is ApiResult.Error -> {
                        emit(DomainResult.Error(result.exception))
                    }
                }
            }
        }
    }

    suspend fun unFollow(followerId: Int): Flow<DomainResult<String>> {
        return flow {
            memberRepository.postUnFollow(followerId).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        emit(DomainResult.Success(result.data))
                    }

                    is ApiResult.Error -> {
                        emit(DomainResult.Error(result.exception))
                    }
                }
            }
        }
    }
}