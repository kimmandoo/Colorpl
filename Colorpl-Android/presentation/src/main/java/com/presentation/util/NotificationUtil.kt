package com.presentation.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.colorpl.presentation.R
import com.domain.model.Setting
import com.presentation.MainActivity
import timber.log.Timber


fun sendNotification(
    context: Context,
    time: String?,
    distance: String?,
    settingsInfo: Setting
) {

    if (!settingsInfo.notificationSend) {
        Timber.tag("NOTI").d("$settingsInfo")
        return
    }

    val intent = Intent(context, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    val pendingIntent = PendingIntent.getActivity(
        context, 0, intent,
        PendingIntent.FLAG_IMMUTABLE
    )


    val channelId = "colorpl"

    val notificationBuilder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_notification_logo)
        .setContentTitle(context.getString(R.string.notification_colorpl_title))
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText("현재 위치로부터 공연 장소까지 걸리는\n시간 : $time \n거리 : $distance\n늦지 않게 출발하세요!")
        )
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)

    // 진동
    if (settingsInfo.notificationVibrate) {
        notificationBuilder.setVibrate(longArrayOf(0, 250, 250, 250))
    }

    // 소리
    if (settingsInfo.notificationSound) {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        notificationBuilder.setSound(defaultSoundUri)
    } else {
        notificationBuilder.setSilent(true)
    }

    val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    // 오레오 이상에서 알림을 제공하려면 앱의 알림 채널을 시스템에 등록해야 한다.
    val channel = NotificationChannel(
        channelId,
        "Channel",
        NotificationManager.IMPORTANCE_HIGH
    ).apply {
        enableVibration(settingsInfo.notificationVibrate)
        setSound(
            if (settingsInfo.notificationSound) RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) else null,
            null
        )
    }
    notificationManager.createNotificationChannel(channel)

    notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
}