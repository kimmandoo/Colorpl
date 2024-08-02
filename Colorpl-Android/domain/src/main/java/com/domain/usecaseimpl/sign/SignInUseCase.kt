package com.domain.usecaseimpl.sign

import com.data.repository.SignRepository
import com.data.repository.TokenRepository
import com.data.util.ApiResult
import com.domain.mapper.toParam
import com.domain.model.User
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val signRepository: SignRepository
) {

    suspend fun signIn(user: User) = flow {
        signRepository.signIn(user.toParam()).collect { result ->
            Timber.d("로그인 확인 $result")
            when (result) {
                is ApiResult.Success -> {
                    val data = DomainResult.success(result.data)
                    tokenRepository.setAccessToken(data.data.accessToken)
                    emit(DomainResult.success(result.data))
                }

                is ApiResult.Error -> {
                    emit(DomainResult.error(result.exception))
                }
            }
        }

    }

}