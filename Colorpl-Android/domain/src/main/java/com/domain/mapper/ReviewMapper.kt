package com.domain.mapper

import com.data.model.request.RequestReviewCreate
import com.data.model.request.RequestReviewEdit
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

fun Review.toEditEntity(): RequestReviewEdit {
    return RequestReviewEdit(
        content = content, spoiler = spoiler, emotion = emotion
    )
}