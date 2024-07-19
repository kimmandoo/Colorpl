package com.presentation.notification

import androidx.navigation.fragment.findNavController
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentNotificationBinding
import com.domain.model.Notification
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.NotificationAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NotificationFragment :
    BaseFragment<FragmentNotificationBinding>(R.layout.fragment_notification) {

    private lateinit var notificationAdapter: NotificationAdapter

    override fun initView() {
        initNotificationAdapter()
        navigatePop()
    }

    private fun initNotificationAdapter() {
        notificationAdapter = NotificationAdapter()
        binding.rcNotification.adapter = notificationAdapter

        notificationAdapter.submitList(Notification.DEFAULT)
    }


    private fun navigatePop(){
        binding.imgBack.setOnClickListener {
            navigatePopBackStack(findNavController())
        }
    }
}