package com.presentation.reservation

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentReservationBinding
import com.domain.model.FilterItem
import com.domain.model.ReservationInfo
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.feed.FilterAdapter
import com.presentation.component.adapter.reservation.ReservationInfoAdapter
import com.presentation.component.dialog.DateRangePickerDialog
import com.presentation.component.dialog.LocationPickerDialog
import com.presentation.util.getFilterItems
import com.presentation.viewmodel.ReservationListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.co.bootpay.android.Bootpay
import kr.co.bootpay.android.events.BootpayEventListener
import kr.co.bootpay.android.models.BootItem
import kr.co.bootpay.android.models.BootUser
import kr.co.bootpay.android.models.Payload
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ReservationFragment :
    BaseFragment<FragmentReservationBinding>(R.layout.fragment_reservation) {


    private val reservationListViewModel: ReservationListViewModel by viewModels()

    private val filterAdapter by lazy {
        FilterAdapter(onItemClickListener = { filterItem ->
            onFilterClickListener(filterItem)
        })
    }

    private val reservationInfoAdapter by lazy {
        ReservationInfoAdapter(
            onClickListener = { reservationInfo ->
                onClickListener(reservationInfo)
            },
        )
    }

    override fun initView() {
        initFilter()
        initReservationInfo()
        initClickListener()
        initReservationList()
        observeReservationList()
    }

    private fun initReservationList() {
        reservationListViewModel.getReservationList()
    }

    private fun observeReservationList() {
        reservationListViewModel.reservationList.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach { reservationList ->
//            reservationInfoAdapter.submitList(reservationList)
            Timber.tag("reservationList").d(reservationList.toString())
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initFilter() {
        binding.rvFilter.apply {
            adapter = filterAdapter
            itemAnimator = null
        }
        filterAdapter.submitList(binding.root.context.getFilterItems())
    }

    private fun initReservationInfo() {
        binding.rvReservationInfo.apply {
            adapter = reservationInfoAdapter
            itemAnimator = null
        }
        val testReservationInfo = mutableListOf<ReservationInfo>()
        repeat(10) {
            testReservationInfo.add(
                ReservationInfo(
                    reservationInfoId = 2,
                    contentImg = null,
                    title = "님과함께 : 테스트",
                    category = "사극",
                    runtime = "3시간 00분",
                    price = "13,000"
                )
            )
        }
        reservationInfoAdapter.submitList(testReservationInfo)
    }

    private fun initClickListener() {
        binding.apply {
            clSelectDate.setOnClickListener {
                showDateRangePickerDialog()
            }
            clSelectLocation.setOnClickListener {
                showLocationPickerDialog()
            }
            ivFilter.setOnClickListener {
                initReservationList()
            }
        }
    }

    private fun onFilterClickListener(clickedItem: FilterItem) {
        val updatedList = filterAdapter.currentList.map { item ->
            if (item.name == clickedItem.name) {
                item.copy(isSelected = true)
            } else {
                item.copy(isSelected = false)
            }
        }

        filterAdapter.submitList(updatedList)
    }

    private fun onClickListener(data: ReservationInfo) {
        val action =
            ReservationFragmentDirections.actionFragmentReservationToFragmentReservationDetail(data)
        Navigation.findNavController(binding.root).navigate(action)
    }

    /** 날짜 선택 캘린더 Dialog */
    private fun showDateRangePickerDialog() {
        Toast.makeText(binding.root.context, "날짜 클릭", Toast.LENGTH_SHORT).show()
        val dateRangePickerDialog = DateRangePickerDialog(requireContext()) { year, month, day ->
            // 날짜 범위를 선택한 후 수행할 작업을 여기에 추가합니다.
            Toast.makeText(
                binding.root.context,
                "년: $year, 월: ${month + 1}, 일: $day",
                Toast.LENGTH_SHORT
            ).show()
            binding.tvSelectDate.text = "$year.${month + 1}.$day"
        }
        dateRangePickerDialog.show()
    }

    /** 지역 선택 리스트 Dialog */
    private fun showLocationPickerDialog() {
        Toast.makeText(binding.root.context, "날짜 클릭", Toast.LENGTH_SHORT).show()
        val locationList = arrayOf("전국", "서울", "경기·인천", "강원", "충청·대전·세종", "경상·대구·울산·부산", "전라·광주")
        val locationPickerDialog =
            LocationPickerDialog(requireContext(), locationList) { selectedCity ->
                binding.tvSelectLocation.text = selectedCity
            }
        locationPickerDialog.show()
    }



}