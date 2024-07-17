package com.presentation.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.colorpl.presentation.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {


    // 메시지 수신
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d("메세지 호출 ${remoteMessage.notification}")
        Timber.d("데이터 메세지 호출 ${remoteMessage.data}")
        sendNotification(remoteMessage)
        initWorkerManager()
    }

    private fun initWorkerManager(){
        val fcmWorkRequest : WorkRequest = OneTimeWorkRequestBuilder<FcmWorker>().build()

        WorkManager.getInstance(this).enqueue(fcmWorkRequest)
    }


    private fun sendNotification(remoteMessage: RemoteMessage) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(com.naver.maps.map.R.drawable.navermap_default_marker_icon_lightblue)
            .setContentTitle(remoteMessage.data.get("title"))
            .setContentText(remoteMessage.data.get("body"))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 이상에서 알림을 제공하려면 앱의 알림 채널을 시스템에 등록해야 한다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }


}