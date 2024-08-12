package com.domain.mapper

import com.data.model.response.ResponseSetting
import com.domain.model.Setting

fun ResponseSetting.toEntity(): Setting {
    return Setting(
        notificationSend = notificationSend,
        notificationVibrate = notificationVibrate,
        notificationSound = notificationSound
    )
}