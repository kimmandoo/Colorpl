package com.domain.usecaseimpl.sign

import com.data.repository.SignRepository
import com.data.util.ApiResult
import com.domain.mapper.toSignUpParam
import com.domain.model.User
import com.domain.util.RepoResult
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val signRepository: SignRepository
) {

    suspend fun signUp(user: User) = flow {
        signRepository.signUp(user.toSignUpParam()).collect { result ->
            Timber.d("회원 가입 확인 $result")
            when (result) {
                is ApiResult.Success -> {
                    val data = RepoResult.success(result.data)
                    emit(RepoResult.success(result.data))
                }

                is ApiResult.Error -> {
                    emit(RepoResult.error(result.exception))
                }
            }
        }

    }
}