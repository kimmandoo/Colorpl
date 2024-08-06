package com.domain.mapper

import com.data.model.request.RequestReviewCreate
import com.domain.model.Review

fun Review.toEntity(): RequestReviewCreate {
    return RequestReviewCreate(
        memberId = memberId,
        ticketId = ticketId,
        content = content,
        spoiler = spoiler,
        emotion = emotion
    )
}