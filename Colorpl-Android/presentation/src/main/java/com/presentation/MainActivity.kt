package com.presentation

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.presentation.base.BaseActivity
import com.presentation.util.LocationHelper
import com.presentation.util.Page
import com.presentation.util.checkLocationPermission
import com.presentation.util.locationPermission
import com.presentation.util.requestMapPermission
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var navController: NavController
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var naverMapCameraPosition: CameraPosition

    override fun init() {
        locationPermission()
        initBottomNavBar()
        initFCM()
        setBottomNavHide()
        getLastLocation()
        LocationHelper.initialize(this).listener = { it ->
            // listener
        }
        LocationHelper.getInstance().startLocationTracking()
    }

    private fun initBottomNavBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            binding.fabTicket.isSelected = destination.id == R.id.fragment_reservation
        }
        binding.apply {
            fabTicket.setOnClickListener {
                binding.fabTicket.isSelected = true
                bottomNavigationBar.selectedItemId = R.id.fragment_reservation
            }
            bottomNavigationBar.background = null
            bottomNavigationBar.setupWithNavController(navController)
            bottomNavigationBar.setOnItemReselectedListener {
            }
        }

    }

    /** 바텀 네비게이션 숨기는 기능 */
    private fun setBottomNavHide() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val page = Page.fromId(destination.id)
            binding.bottomVisibility = page?.hideBottomNav != true
        }
    }

    /** FCM SDK 초기화 */
    private fun initFCM() {
        FirebaseApp.initializeApp(this);

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String> ->
                if (!task.isSuccessful) {
                    Timber.d("호요요")
                    return@addOnCompleteListener
                }
            }
    }

    /** 사용자 마지막 위치 가져오기 */
    private fun getLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        Timber.d("MainActivity getLastLocation()")
        checkLocationPermission(applicationContext)
        this.requestMapPermission {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                val loc = if (location == null) {
                    LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
                } else {
                    LatLng(location.latitude, location.longitude)
                }
                naverMapCameraPosition = CameraPosition(loc, DEFAULT_ZOOM)
                Timber.d("naverMapCameraPosition : $naverMapCameraPosition")
            }
        }
    }

    companion object {
        private const val DEFAULT_LATITUDE = 37.5666805
        private const val DEFAULT_LONGITUDE = 126.9784147
        private const val DEFAULT_ZOOM = 15.0
    }
}