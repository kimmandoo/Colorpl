package com.presentation.reservation

import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentReservationBinding
import com.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ReservationFragment: BaseFragment<FragmentReservationBinding>(R.layout.fragment_reservation) {

    @Inject
    @Named("bootpay")
    lateinit var bootpayKey: String

    override fun initView() {
        Timber.d("$bootpayKey")
    }
}