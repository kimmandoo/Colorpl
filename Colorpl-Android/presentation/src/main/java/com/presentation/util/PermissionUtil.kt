package com.presentation.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.colorpl.presentation.R
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

// 카메라 권한 허용
fun checkCameraPermission(action: () -> Unit) {
    TedPermission.create().setPermissionListener(object : PermissionListener {
        override fun onPermissionGranted() {
            action()
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {

        }
    }).setDeniedMessage("앱을 사용하려면 권한이 필요합니다. [설정] > [권한]에서 권한을 허용해 주세요.")
        .setPermissions(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        .check()
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

/**  */
fun checkLocationPermission(context: Context) {
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }
}




fun Context.requestMapPermission(complete: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        TedPermission.create()
            .setDeniedMessage(this.getString(R.string.permission_denied_message)) // 권한이 없을 때 띄워주는 Dialog Message
            .setDeniedCloseButtonText(this.getString(R.string.permission_denied_closed_button_message))
            .setGotoSettingButtonText(this.getString(R.string.permission_go_to_setting_button_message))
            .setPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() { // 권한이 허용됐을 때
                    Timber.d("권한 허용 완료!")
                    complete()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) { // 권한이 거부됐을 때
                    Timber.d("권한 허용이 거부됨")
                }
            })
            .check()
    }
}