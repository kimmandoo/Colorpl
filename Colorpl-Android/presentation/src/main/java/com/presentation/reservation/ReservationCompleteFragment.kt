package com.presentation.reservation

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentReservationCompleteBinding
import com.presentation.base.BaseFragment
import com.presentation.viewmodel.ReservationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class ReservationCompleteFragment :
    BaseFragment<FragmentReservationCompleteBinding>(R.layout.fragment_reservation_complete) {
    private val reservationViewModel: ReservationViewModel by viewModels({ requireParentFragment() })

    override fun initView() {
        initClickEvent()
        initData()
    }

    private fun initData() {
        reservationViewModel.payResult.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                binding.payResult = reservationViewModel.payResult.value
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }


    private fun initClickEvent() {
        binding.apply {
            tvComplete.setOnClickListener {
                navigatePopBackStack()
            }
        }
    }
}