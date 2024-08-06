package com.domain.mapper

import com.data.model.paging.Feed

fun Feed.toEntity(): com.domain.model.Feed {
    return com.domain.model.Feed(
        category = category,
        commentPageSize = this.commentpagesize,
        commentsCount = this.commentscount,
        content = content,
        createDate = createdate,
        emotionMode = emotion,
        empathyCount = empathy,
        id = id,
        contentImgUrl = imgurl,
        isEmpathy = myempathy,
        isMyReview = myreview,
        isSpoiler = spoiler,
        ticketId = ticketId,
        title = title,
        writer = writer

    )
}