package com.domain.mapper

import com.data.model.paging.Comment
import com.data.model.paging.Feed

fun Comment.toEntity(): com.domain.model.Comment {
    return com.domain.model.Comment(
        commentId = commentId,
        name = name,
        uploadDate = uploadDate,
        lastEditDate = lastEditDate,
        content = content
    )
}