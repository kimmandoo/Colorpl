package com.presentation.reservation

import android.widget.Toast
import androidx.appcompat.widget.SearchView
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
import com.presentation.util.Category
import com.presentation.util.ShowType
import com.presentation.util.getFilterItems
import com.presentation.util.hideKeyboard
import com.presentation.util.setVisibility
import com.presentation.util.toLocalDate
import com.presentation.viewmodel.ReservationListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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
        initViewModel()
        observeReservationList()
        initSearchWindow()
    }


    private fun observeReservationList() {
        reservationListViewModel.reservationList.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { reservationList ->
                binding.apply {
                    icEmptyView.clTitle.setVisibility(reservationList.isEmpty())
                    rvReservationInfo.setVisibility(reservationList.isNotEmpty())
                }

                reservationInfoAdapter.submitList(reservationList)
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

    private fun initViewModel() {
        binding.viewModel = reservationListViewModel
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
                    priceBySeatClass = mapOf()
                )
            )
        }
//        reservationInfoAdapter.submitList(testReservationInfo)
    }

    private fun initClickListener() {
        binding.apply {
            clSelectDate.setOnClickListener {
                showDateRangePickerDialog()
            }
            clSelectLocation.setOnClickListener {
                showLocationPickerDialog()
            }
            tvClear.setOnClickListener {
                svSearch.clearFocus()
                svSearch.setQuery("", false)
                reservationListViewModel.dataClear()
                onFilterClickListener(FilterItem("전체"))

                viewLifecycleOwner.lifecycleScope.launch {
                    delay(300L)
                    binding.rvReservationInfo.scrollToPosition(0)
                }
            }
        }
    }

    private fun onFilterClickListener(clickedItem: FilterItem) {
        val updatedList = filterAdapter.currentList.map { item ->
            if (item.name == clickedItem.name) {
                Timber.d("확인 $item")
                reservationListViewModel.setParam(
                    ShowType.CATEGORY,
                    Category.getTitle(item.name)?.getKey() ?: ""
                )
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




    private fun initSearchWindow() {
        binding.apply {
            svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    // 검색 버튼을 눌렀을 때 호출
                    query?.let {
                        reservationListViewModel.setParam(ShowType.KEYWORD, it)
                    } ?: run {
                        reservationListViewModel.dataClear()
                        binding.svSearch.clearFocus()
                        requireActivity().hideKeyboard(binding.root)
                    }
                    requireActivity().hideKeyboard(binding.root)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    // 검색 쿼리가 변경될 때 호출
                    newText?.let {
                        Timber.tag("search").d(it)
                    }
                    return true
                }
            })
        }
    }

    /** 날짜 선택 캘린더 Dialog */
    private fun showDateRangePickerDialog() {
        val initialDate = reservationListViewModel.date.value ?: LocalDate.now()
        Timber.tag("date").d(initialDate.toString())
        val dateRangePickerDialog =
            DateRangePickerDialog(requireContext(), initialDate) { year, month, day ->
                // 날짜 범위를 선택한 후 수행할 작업을 여기에 추가합니다.
                val selectedDate = LocalDate.of(year, month, day)
                reservationListViewModel.setDate(selectedDate)
                val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREAN)
                val data = selectedDate.format(dateFormat)
                binding.tvSelectDate.text = data
                reservationListViewModel.setParam(ShowType.DATE, data)
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
                reservationListViewModel.setParam(ShowType.LOCATION, selectedCity)
            }
        locationPickerDialog.show()
    }


}