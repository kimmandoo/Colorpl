package com.domain.mapper

import com.data.model.request.RequestReviewCreate
import com.data.model.request.RequestReviewEdit
import com.data.model.response.ResponseReviewDetail
import com.domain.model.Review
import com.domain.model.ReviewDetail

fun Review.toEntity(): RequestReviewCreate {
    return RequestReviewCreate(
        memberId = memberId,
        ticketId = ticketId,
        content = content,
        spoiler = spoiler,
        emotion = emotion
    )
}


fun ResponseReviewDetail.toEntity(): ReviewDetail {
    return ReviewDetail(
        category = this.category,
        commentPageSize = this.commentpagesize,
        commentCount = this.commentscount,
        content = this.content,
        createDate = this.createdate,
        emotion = this.emotion,
        empathy = this.empathy,
        id = this.id,
        imgUrl = this.imgurl,
        myEmpathy = this.myempathy,
        myReview = this.myreview,
        spoiler = this.spoiler,
        ticketId = this.ticketId,
        title = this.title,
        writer = this.writer
    )
}

fun Review.toEditEntity(): RequestReviewEdit {
    return RequestReviewEdit(
        content = content, spoiler = spoiler, emotion = emotion
    )
}