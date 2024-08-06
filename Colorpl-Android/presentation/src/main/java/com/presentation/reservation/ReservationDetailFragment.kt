package com.presentation.reservation

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentReservationDetailBinding
import com.presentation.base.BaseFragment
import com.presentation.component.dialog.LoadingDialog
import com.presentation.util.TopButtonsStatus
import com.presentation.viewmodel.ReservationDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ReservationDetailFragment : BaseFragment<FragmentReservationDetailBinding>(R.layout.fragment_reservation_detail) {
    private val args: ReservationDetailFragmentArgs by navArgs()
    private val viewModel: ReservationDetailViewModel by viewModels()

    override fun initView() {
        initClickEvent()
        initUi()
        initReservationInfo()
    }

    private fun initUi() {
        binding.apply {
            type = TopButtonsStatus.BACK
            viewModel = this@ReservationDetailFragment.viewModel
        }
    }

    private fun initClickEvent() {
        binding.includeTopCenter.ivBack.setOnClickListener {
            navigatePopBackStack()
        }
        binding.tvReservationNext.setOnClickListener {
            val action = ReservationDetailFragmentDirections.actionFragmentReservationDetailToFragmentReservationProgress(args.reservationDetail)
            Navigation.findNavController(binding.root).navigate(action)
        }
    }

    private fun initReservationInfo() {
        viewModel.getReservationInfo(1)
        viewModel.reservationInfo.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach { reservationInfo ->
            viewModel.reservationInfo.value = reservationInfo
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

}