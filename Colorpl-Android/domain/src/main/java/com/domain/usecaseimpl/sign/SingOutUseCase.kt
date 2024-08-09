package com.domain.usecaseimpl.sign

import com.data.repository.SignRepository
import com.data.repository.TokenRepository
import com.data.util.ApiResult
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class SingOutUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val signRepository: SignRepository
) {
    suspend fun signOut() = flow {
        tokenRepository.getSignToken().collect {
            when (it) {
                is ApiResult.Error -> {
                    Timber.tag("signout").d("${it.exception}")
                    emit(DomainResult.error(it.exception))
                }

                is ApiResult.Success -> {
                    signRepository.signOut(it.data.refreshToken).collect { result ->
                        when (result) {
                            is ApiResult.Success -> {
                                Timber.tag("signout fin").d("${result.data}")
                                tokenRepository.clearToken()
                                emit(DomainResult.success(result.data))
                            }

                            is ApiResult.Error -> {
                                Timber.tag("signout err").d("${result.exception}")
                                emit(DomainResult.error(result.exception))
                            }
                        }
                    }
                }
            }
        }
    }
}