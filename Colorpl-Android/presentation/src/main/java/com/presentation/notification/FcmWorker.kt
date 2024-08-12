package com.presentation.notification

import android.annotation.SuppressLint
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.domain.usecase.TmapRouteUseCase
import com.domain.usecaseimpl.setting.SettingUseCase
import com.domain.util.DomainResult
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.presentation.util.distanceChange
import com.presentation.util.roadTimeChange
import com.presentation.util.sendNotification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber


@HiltWorker
class FcmWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val tMapRouteUseCase: TmapRouteUseCase,
    private val settingUseCase: SettingUseCase
) : CoroutineWorker(context, workerParams) {


    private val mFusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    override suspend fun doWork(): Result {
        val latLng = inputData.getString("latLng").toString()
        val data = latLng.split(",")

        checkLocation { latitude, longitude ->
            val la = latitude
            val long = longitude
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.IO) {
                    tMapRouteUseCase.invoke(long.toString(), la.toString(), data[1], data[0])
                        .collectLatest { result ->
                            val setting = settingUseCase.getSettingsInfo().first()
                            when (result) {
                                is DomainResult.Success -> {
                                    val data = result.data

                                    sendNotification(
                                        context = context, data?.totalTime?.roadTimeChange(),
                                        data?.totalDistance?.distanceChange(),
                                        setting
                                    )
                                }

                                is DomainResult.Error -> {
                                    Timber.d("알림 등록 에러 ${result.exception}")
                                }
                            }

                        }

                }

            }
        }
        return Result.success()
    }

    @SuppressLint("MissingPermission")
    fun checkLocation(listener: (Double, Double) -> Unit) {
        mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
            runCatching {
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