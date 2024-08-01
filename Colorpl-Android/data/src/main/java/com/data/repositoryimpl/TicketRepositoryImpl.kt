package com.data.repositoryimpl

import com.data.api.safeApiCall
import com.data.datasource.remote.TicketDataSource
import com.data.model.request.RequestTicketCreate
import com.data.model.response.ResponseTicketCreate
import com.data.repository.TicketRepository
import com.data.util.ApiResult
import com.data.util.FormDataConverterUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

class TicketRepositoryImpl @Inject constructor(
    private val ticketDataSource: TicketDataSource
) : TicketRepository {
    override suspend fun createTicket(
        ticket: File,
        request: RequestTicketCreate
    ): Flow<ApiResult<ResponseTicketCreate>> = flow {
        emit(safeApiCall {
            val requestPart = FormDataConverterUtil.getJsonRequestBody(request)
            val filePart: MultipartBody.Part =
                FormDataConverterUtil.getMultiPartBody("file", ticket)
            ticketDataSource.createTicket(ticket = filePart, request = requestPart)
        })
    }
}