package com.presentation.util

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner

/**
 *  뒤로가기할 때 행동 할 것
 *  @param owner : 사용할 Fragment
 */
fun FragmentActivity.onBackButtonPressed(owner: LifecycleOwner, action: ()->Unit){
    onBackPressedDispatcher.addCallback(owner, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            action()
        }
    })
}