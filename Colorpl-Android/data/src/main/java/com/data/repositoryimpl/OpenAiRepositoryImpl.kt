package com.data.repositoryimpl

import com.data.api.safeApiCall
import com.data.datasource.OpenAiDataSource
import com.data.model.request.Content
import com.data.model.request.ImageUrl
import com.data.model.request.Message
import com.data.model.request.RequestVision
import com.data.model.response.ResponseVision
import com.data.repository.OpenAiRepository
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OpenAiRepositoryImpl @Inject constructor(
    private val openAiDataSource: OpenAiDataSource,
) : OpenAiRepository {
    override suspend fun getChatCompletion(base64String: String): Flow<ApiResult<ResponseVision>> =
        flow {
            emit(safeApiCall {
                val content = mutableListOf<Content>()
                content.add(
                    Content(
                        image_url = null,
                        text = "사진 속 일정을 알려줘. 제목,장소,일시,좌석 정보들을 줄바꿈하면서 알려줘. 일시는 yyyy년 MM월 dd일 HH:mm 패턴으로 주고 없는 항목은 없음이라고 줘",
                        type = "text"
                    )
                )
                content.add(
                    Content(
                        image_url = ImageUrl(base64String),
                        text = null,
                        type = "image_url"
                    )
                )
                val message = Message(content = content, role = "user")
                openAiDataSource.getChatCompletion(
                    RequestVision(
                        max_tokens = 80,
                        messages = listOf(message),
                        model = "gpt-4o-mini"
                    )
                )
            })
        }
}