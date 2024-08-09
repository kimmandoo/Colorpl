package com.domain.usecaseimpl.member

import com.data.repository.MemberRepository
import com.data.util.ApiResult
import com.domain.mapper.toEntity
import com.domain.model.Member
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMemberInfoUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    suspend fun getMember(type: Boolean = false, memberId: Int = 0): Flow<DomainResult<Member>> {
        return flow {
            if (type) {
                memberRepository.getOtherMember(memberId).collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            emit(DomainResult.success(result.data.toEntity()))
                        }

                        is ApiResult.Error -> {
                            emit(DomainResult.error(result.exception))
                        }
                    }
                }
            } else {
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

    suspend fun getFollowerCount(
        type: Boolean = false,
        memberId: Int = 0
    ): Flow<DomainResult<Int>> {
        return flow {
            if (type) {
                memberRepository.getOtherFollowersCount(memberId).collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            emit(DomainResult.success(result.data.count))
                        }

                        is ApiResult.Error -> {
                            emit(DomainResult.error(result.exception))
                        }
                    }
                }
            } else {
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
    }

    suspend fun getFollowingCount(
        type: Boolean = false,
        memberId: Int = 0
    ): Flow<DomainResult<Int>> {
        return flow {
            if (type) {
                memberRepository.getOtherFollowingCount(memberId).collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            emit(DomainResult.success(result.data.count))
                        }

                        is ApiResult.Error -> {
                            emit(DomainResult.error(result.exception))
                        }
                    }
                }
            } else {
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
    }

    suspend fun getMemberInfo(
        type: Boolean = false,
        memberId: Int = 0,
    ): Flow<DomainResult<Member>> {

        return flow {
            combine(
                getMember(type, memberId),
                getFollowerCount(type, memberId),
                getFollowingCount(type, memberId)
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
            }.collect { result ->
                emit(result)
            }
        }
    }

}