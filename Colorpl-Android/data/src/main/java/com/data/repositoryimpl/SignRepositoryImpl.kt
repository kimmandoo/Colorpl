package com.data.repositoryimpl

import com.data.datasource.local.TokenDataSource
import com.data.repository.SignRepository
import javax.inject.Inject

class SignRepositoryImpl @Inject constructor(
    private val tokenDataSource: TokenDataSource
) : SignRepository {


}