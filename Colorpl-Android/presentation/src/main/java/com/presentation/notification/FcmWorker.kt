package com.presentation.notification

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import timber.log.Timber


class FcmWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val mFusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    override suspend fun doWork(): Result {
            checkLocation()
        return Result.success()
    }

    @SuppressLint("MissingPermission")
    fun checkLocation(){
        mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
            kotlin.runCatching {
                task.result
            }.onSuccess {location ->
                Timber.d("위치 불러오기 성공 ${location.latitude}, ${location.longitude}")

            }.onFailure { e ->
                Timber.d("위치 불러오기 에러 확인 $e")

            }
        }
    }
}