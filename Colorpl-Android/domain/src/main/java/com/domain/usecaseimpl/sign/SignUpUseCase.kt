package com.domain.usecaseimpl.sign

import com.data.repository.SignRepository
import com.data.repository.TokenRepository
import com.data.util.ApiResult
import com.domain.mapper.toSignTokenParam
import com.domain.mapper.toSignUpParam
import com.domain.model.Member
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val signRepository: SignRepository,
    private val tokenRepository: TokenRepository
) {

    suspend fun signUp(member: Member, file: File?) = flow {
        signRepository.signUp(member.toSignUpParam(), file).collect { result ->
            Timber.d("회원 가입 확인 $result")
            when (result) {
                is ApiResult.Success -> {
                    tokenRepository.setSignToken(member.toSignTokenParam())
                    emit(DomainResult.success(result.data))
                }

                is ApiResult.Error -> {
                    emit(DomainResult.error(result.exception))
                }
            }
        }

    }
}