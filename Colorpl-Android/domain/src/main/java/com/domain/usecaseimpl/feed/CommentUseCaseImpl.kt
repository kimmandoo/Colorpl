package com.domain.usecaseimpl.feed

import androidx.paging.PagingData
import androidx.paging.map
import com.data.repository.CommentRepository
import com.domain.mapper.toEntity
import com.domain.model.Comment
import com.domain.usecase.CommentUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CommentUseCaseImpl @Inject constructor(private val commentRepository: CommentRepository) :
    CommentUseCase {
    override fun invoke(feedId: Int): Flow<PagingData<Comment>> {
        return commentRepository.getPagedComment(feedId)
            .map { pagingData ->
                pagingData.map { comment ->
                    comment.toEntity()
                }
            }
    }
}