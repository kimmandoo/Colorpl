package com.presentation.schedule

import androidx.navigation.fragment.findNavController
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentScheduleBinding
import com.presentation.base.BaseFragment

class ScheduleFragment: BaseFragment<FragmentScheduleBinding>(R.layout.fragment_schedule) {

    override fun initView() {
        navigateNotification()
    }



    private fun navigateNotification(){
        binding.imgNotification.setOnClickListener {
            navigateDestination(findNavController(), R.id.action_fragment_schedule_to_fragment_notification)
        }
    }
}