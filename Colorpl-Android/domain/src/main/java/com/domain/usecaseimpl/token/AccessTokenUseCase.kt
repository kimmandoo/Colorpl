package com.domain.usecaseimpl.token

import com.data.repository.TokenRepository
import com.data.util.ApiResult
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AccessTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend fun getSignToken() = flow{
        tokenRepository.getSignToken().collect { result ->
            when (result) {
                is ApiResult.Success -> {
                    emit(DomainResult.success(result.data))
                }

                is ApiResult.Error -> {
                    emit(DomainResult.error(result.exception))
                }
            }
        }
    }


    suspend fun getAccessToken() = flow {
        tokenRepository.getAccessToken().collect { result ->
            when (result) {
                is ApiResult.Success -> {
                    emit(DomainResult.success(result.data))
                }

                is ApiResult.Error -> {
                    emit(DomainResult.error(result.exception))
                }
            }
        }
    }

    suspend fun setAccessToken(accessToken : String){
        tokenRepository.setAccessToken(accessToken)
    }

}