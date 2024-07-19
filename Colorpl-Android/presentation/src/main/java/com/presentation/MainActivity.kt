package com.presentation

import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.ActivityMainBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var navController: NavController

    override fun init() {
        initBottomNavBar()
        initFCM()
        setBottomNavHide()
    }

    private fun initBottomNavBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            binding.fabTicket.isSelected = destination.id == R.id.fragment_ticket
        }
        binding.apply {
            fabTicket.setOnClickListener {
                binding.fabTicket.isSelected = true
                bottomNavigationBar.selectedItemId = R.id.fragment_ticket
            }
            bottomNavigationBar.background = null
            bottomNavigationBar.setupWithNavController(navController)
            bottomNavigationBar.setOnItemReselectedListener {
            }
        }

    }

    private fun setBottomNavHide(){ //바텀 네비게이션 숨기는 기능
        navController.addOnDestinationChangedListener{_, destination, _ ->
            binding.bottomVisibility = when(destination.id){
                R.id.fragment_notification -> false
                else -> true
            }
        }
    }

    private fun initFCM() {
        // FCM SDK 초기화
        FirebaseApp.initializeApp(this);

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String> ->
                val token = task.result
                Timber.d("FCM Log ${token}")
                if (!task.isSuccessful) {
                    Timber.d("호요요")
                    return@addOnCompleteListener
                }

            }
    }
}