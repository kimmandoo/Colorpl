package com.presentation.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.colorpl.presentation.*
import com.presentation.MainActivity


fun sendNotification(context: Context, time: String?, distance: String?) {
    val intent = Intent(context, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    val pendingIntent = PendingIntent.getActivity(
        context, 0, intent,
        PendingIntent.FLAG_IMMUTABLE
    )


    val channelId = "colorpl"

    val notificationBuilder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_colorpl_logo)
        .setContentTitle(context.getString(R.string.notification_colorpl_title))
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText("현재 위치로부터 공연 장소까지 걸리는\n시간 : $time \n거리 : $distance\n늦지 않게 출발하세요!")
        )
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)

    val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    // 오레오 이상에서 알림을 제공하려면 앱의 알림 채널을 시스템에 등록해야 한다.
    val channel = NotificationChannel(
        channelId,
        "Channel",
        NotificationManager.IMPORTANCE_HIGH
    )
    notificationManager.createNotificationChannel(channel)

    notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())

}