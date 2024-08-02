package com.domain.usecase

import com.data.model.request.RequestVision
import com.data.model.response.ResponseTicketCreate
import com.data.model.response.ResponseVision
import com.domain.model.Description
import com.domain.model.Ticket
import com.domain.util.RepoResult
import kotlinx.coroutines.flow.Flow
import java.io.File

interface TicketCreateUseCase {
    suspend operator fun invoke(
        image: File,
        ticket: Ticket
    ): Flow<RepoResult<String>>
}