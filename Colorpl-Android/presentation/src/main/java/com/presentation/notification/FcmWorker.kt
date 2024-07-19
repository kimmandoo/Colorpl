package com.presentation.notification

import android.annotation.SuppressLint
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.domain.usecase.NotificationUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltWorker
class FcmWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationUseCase : NotificationUseCase
) :
    CoroutineWorker(context, workerParams) {


    private val mFusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    override suspend fun doWork(): Result {
        checkLocation { latitude, longitude ->
            val la = latitude
            val long = longitude
            CoroutineScope(Dispatchers.IO).launch {
                notificationUseCase.getMarkers(latitude = la, long, 2.0)
            }
        }
        return Result.success()
    }

    @SuppressLint("MissingPermission")
    fun checkLocation(listener: (Double, Double) -> Unit) {
        mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
            kotlin.runCatching {
                task.result
            }.onSuccess { location ->
                listener(location.latitude, location.longitude)
                Timber.d("위치 불러오기 성공 ${location.latitude}, ${location.longitude}")

            }.onFailure { e ->
                Timber.d("위치 불러오기 에러 확인 $e")

            }
        }
    }
}