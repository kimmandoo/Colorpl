package com.presentation.reservation

import androidx.navigation.fragment.navArgs
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentReservationDetailBinding
import com.presentation.base.BaseFragment
import com.presentation.util.TopButtonsStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReservationDetailFragment : BaseFragment<FragmentReservationDetailBinding>(R.layout.fragment_reservation_detail) {
    private val args: ReservationDetailFragmentArgs by navArgs()

    override fun initView() {
        initClickEvent()
        initUi()
    }

    private fun initUi() {
        binding.reservationInfo = args.reservationDetail
        binding.type = TopButtonsStatus.BACK
    }

    private fun initClickEvent() {
        binding.includeTopCenter.ivBack.setOnClickListener {
            navigatePopBackStack()
        }
        binding.tvReservationNext.setOnClickListener {
            navigateDestination(R.id.action_fragment_reservation_detail_to_fragment_reservation_progress)
        }
    }
}