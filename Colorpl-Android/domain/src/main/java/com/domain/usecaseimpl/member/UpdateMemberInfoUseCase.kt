package com.domain.usecaseimpl.member

import com.data.repository.MemberRepository
import com.data.util.ApiResult
import com.domain.mapper.toRequestMemberInfo
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class UpdateMemberInfoUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {

    suspend fun updateMemberInfo(nickname: String, file: File?) = flow {
        memberRepository.updateMemberInfo(nickname.toRequestMemberInfo(), file).collect { result ->
            when (result) {
                is ApiResult.Success -> {
                    Timber.d("프로필 수정 성공  ${result.data}")
                    emit(DomainResult.success(result.data))
                }

                is ApiResult.Error -> {
                    emit(DomainResult.error(result.exception))
                }
            }
        }

    }
}