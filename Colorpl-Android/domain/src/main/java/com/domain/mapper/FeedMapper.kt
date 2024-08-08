package com.domain.mapper

import com.data.model.paging.Feed

fun Feed.toEntity(): com.domain.model.Feed {
    return com.domain.model.Feed(
        category = this.category,
        commentpagesize = this.commentpagesize,
        commentscount = this.commentscount,
        content = this.content,
        createdate = this.createdate,
        emotion = this.emotion,
        empathy = this.empathy,
        id = this.id,
        imgurl = this.imgurl,
        myempathy = this.myempathy,
        myreview = this.myreview,
        scheduleId = this.scheduleId,
        spoiler = this.spoiler,
        title = this.title,
        writer = this.writer
    )
}