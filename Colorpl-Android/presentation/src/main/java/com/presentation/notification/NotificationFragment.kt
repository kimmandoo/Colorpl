package com.presentation.notification

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentNotificationBinding
import com.domain.model.Notification
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.notification.NotificationAdapter
import com.presentation.component.custom.ItemTouchHelperCallback
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

        val helper = ItemTouchHelper(ItemTouchHelperCallback(notificationAdapter))
        helper.attachToRecyclerView(binding.rcNotification)

        notificationAdapter.submitList(Notification.DEFAULT)
    }


    private fun navigatePop() {
        binding.imgBack.setOnClickListener {
            navigatePopBackStack(findNavController())
        }
    }
}
