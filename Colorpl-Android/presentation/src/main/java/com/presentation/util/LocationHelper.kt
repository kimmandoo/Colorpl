package com.presentation.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.naver.maps.map.util.FusedLocationSource
import timber.log.Timber


class LocationHelper private constructor(private val context: Context){
    private var timeInterval: Long = FASTEST_UPDATE_INTERVAL.toLong()
    private var request: LocationRequest
    private var locationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private var currentLocation: Location? = null
    lateinit var listener: (Location) -> Unit

    init {
        request = createRequest()
    }

    fun updateTimeInterval(timeInterval: Long){
        this.timeInterval = timeInterval
        request = createRequest()
    }

    fun getFusedLocationSource(): FusedLocationSource{
        return FusedLocationSource(context as Activity, LOCATION_PERMISSION_REQUEST_CODE)
    }

    fun getClient(): FusedLocationProviderClient {
        return  locationClient
    }

    fun getCurrentLocation(): Location? {
        return currentLocation
    }

    private fun createRequest(): LocationRequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, timeInterval).apply {
            setMinUpdateDistanceMeters(10.0f)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()

    fun startLocationTracking() {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                @SuppressLint("MissingPermission")
                override fun onPermissionGranted() {
                    locationClient.lastLocation.addOnSuccessListener {
                        listener(it)
                    }
                    locationClient.requestLocationUpdates(
                        request,
                        this@LocationHelper.LocationCallBack(),
                        null
                    )
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Timber.d("onPermissionDenied 위치 권한 거부")
                }
            })
            .setDeniedMessage("위치 권한을 허용해주세요.")
            .setPermissions(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ).check()
    }

    fun stopLocationTracking() {
        locationClient.flushLocations()
        locationClient.removeLocationUpdates(this.LocationCallBack())
    }

    inner class LocationCallBack() : LocationCallback() {
        override fun onLocationResult(location: LocationResult) {
            super.onLocationResult(location)
            val locationList = location.locations
            if (locationList.size > 0) {
                currentLocation = locationList[locationList.size - 1]
                currentLocation?.let {
                    listener(it)
                    Timber.d("onLocationResult: lng: ${it.longitude} lat: ${it.latitude}")
                }
            }
        }

        override fun onLocationAvailability(availability: LocationAvailability) {
            super.onLocationAvailability(availability)
        }
    }


    companion object {
        private var instance: LocationHelper? = null
        fun initialize(context: Context): LocationHelper {
            if (instance == null) {
                synchronized(LocationHelper::class.java) {
                    if (instance == null) {
                        instance = LocationHelper(context)
                        Timber.d("getInstance: create with context")
                    }
                }
            }
            return instance!!
        }

        fun getInstance(): LocationHelper {
            return instance!!
        }
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        const val FASTEST_UPDATE_INTERVAL = 500
    }
}