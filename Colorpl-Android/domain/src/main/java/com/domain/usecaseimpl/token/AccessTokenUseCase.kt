package com.domain.usecaseimpl.token

import com.data.repository.TokenRepository
import com.data.util.ApiResult
import com.domain.util.RepoResult
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AccessTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {

    suspend fun getAccessToken() = flow {
        tokenRepository.getAccessToken().collect { result ->
            when (result) {
                is ApiResult.Success -> {
                    emit(RepoResult.success(result.data))
                }

                is ApiResult.Error -> {
                    emit(RepoResult.error(result.exception))
                }
            }
        }
    }

    suspend fun setAccessToken(accessToken : String){
        tokenRepository.setAccessToken(accessToken)
    }

}