package com.presentation.reservation

import androidx.fragment.app.viewModels
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentReservationCompleteBinding
import com.presentation.base.BaseFragment
import com.presentation.viewmodel.ReservationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ReservationCompleteFragment :
    BaseFragment<FragmentReservationCompleteBinding>(R.layout.fragment_reservation_complete) {
    private val viewModel: ReservationViewModel by viewModels({ requireParentFragment() })
    override fun initView() {

    }
}