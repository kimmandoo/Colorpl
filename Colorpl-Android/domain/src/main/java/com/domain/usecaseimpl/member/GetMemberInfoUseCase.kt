package com.domain.usecaseimpl.member

import com.data.repository.MemberRepository
import com.data.util.ApiResult
import com.domain.mapper.toEntity
import com.domain.model.Member
import com.domain.util.DomainResult
import com.domain.util.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.zip
import timber.log.Timber
import javax.inject.Inject

class GetMemberInfoUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    suspend fun getMember(): Flow<DomainResult<Member>> {
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

    suspend fun getFollowerCount(): Flow<DomainResult<Int>> {
        return flow {
            memberRepository.getFollowerCount().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        emit(DomainResult.success(result.data.count))
                    }

                    is ApiResult.Error -> {
                        emit(DomainResult.error(result.exception))
                    }
                }
            }
        }
    }

    suspend fun getFollowingCount(): Flow<DomainResult<Int>> {
        return flow {
            memberRepository.getFollowingCount().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        emit(DomainResult.success(result.data.count))
                    }

                    is ApiResult.Error -> {
                        emit(DomainResult.error(result.exception))
                    }
                }
            }
        }
    }

    suspend fun getMemberInfo(): Flow<DomainResult<Member>> {
        return flow {
            combine(
                getMember(),
                getFollowerCount(),
                getFollowingCount()
            ) { memberResult, followerCountResult, followingCountResult ->

                val member = when (memberResult) {
                    is DomainResult.Success -> memberResult.data
                    is DomainResult.Error -> return@combine DomainResult.error(memberResult.exception)
                }

                val followerCount = when (followerCountResult) {
                    is DomainResult.Success -> followerCountResult.data
                    is DomainResult.Error -> return@combine DomainResult.error(followerCountResult.exception)
                }

                val followingCount = when (followingCountResult) {
                    is DomainResult.Success -> followingCountResult.data
                    is DomainResult.Error -> return@combine DomainResult.error(followingCountResult.exception)
                }

                val completeMember = member.copy(
                    followerCount = followerCount,
                    followingCount = followingCount
                )

                DomainResult.success(completeMember)
            }.collect{result ->
                emit(result)
            }
        }
    }

}