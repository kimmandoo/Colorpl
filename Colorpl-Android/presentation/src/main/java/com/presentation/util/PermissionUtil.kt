package com.presentation.util

import android.os.Build
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import timber.log.Timber

//알림 권한 허용
fun notificationPermission(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        TedPermission.create()
            .setDeniedMessage("알림 권한을 허용해주세요.")
            .setDeniedCloseButtonText("닫기")
            .setGotoSettingButtonText("설정")
            .setPermissions(
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {

                }

                //권한이 거부됐을 때
                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {

                }
            })
            .check()
    }
}

//위치 권한 허용
fun locationPermission(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        TedPermission.create()
            .setDeniedMessage("위치 권한을 허용해주세요.")
            .setDeniedCloseButtonText("닫기")
            .setGotoSettingButtonText("설정")
            .setPermissions(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .setPermissionListener(object : PermissionListener {

                override fun onPermissionGranted() {
                    Timber.d("위치 권한 허용")
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {

                }
            })
            .check()
    }
}