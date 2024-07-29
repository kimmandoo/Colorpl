package com.domain.mapper

import com.data.model.paging.Feed

fun Feed.toEntity(): com.domain.model.Feed {
    return com.domain.model.Feed(
        feedId = this.feedId,
        title = this.title,
        userName = this.userName,
        userProfileImg = this.userProfileImg,
        contentImg = this.contentImg,
        emotionMode = this.emotionMode,
        emotionTotal = this.emotionTotal,
        commentTotal = this.commentTotal,
        uploadedDate = this.uploadedDate
    )
}