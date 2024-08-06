package com.domain.mapper

import com.data.model.paging.Feed

fun Feed.toEntity(): com.domain.model.Feed {
    return com.domain.model.Feed(
        category = category,
        commentPageSize = commentPageSize,
        commentsCount = commentsCount,
        content = content,
        createDate = createDate,
        emotionMode = emotionMode,
        empathyCount = empathyCount,
        id = id,
        contentImgUrl = contentImgUrl,
        isEmpathy = isEmpathy,
        isMyReview = isMyReview,
        isSpoiler = isSpoiler,
        ticketId = ticketId,
        title = title,
        writer = writer
    )
}