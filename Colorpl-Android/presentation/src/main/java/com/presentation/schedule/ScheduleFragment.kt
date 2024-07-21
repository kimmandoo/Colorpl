package com.presentation.schedule

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentScheduleBinding
import com.domain.model.CalendarItem
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.schedule.CalendarAdapter
import com.presentation.util.Calendar
import com.presentation.util.createCalendar
import com.presentation.viewmodel.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentScheduleBinding>(R.layout.fragment_schedule) {

    private val viewModel: ScheduleViewModel by viewModels()
    private val calendarAdapter by lazy {
        CalendarAdapter(onItemClick = { calendarItem ->
            onDateClick(calendarItem)
            handleItemClick(calendarItem)
        })
    }
    private var selectedDate = LocalDate.now()

    override fun initView() {
        navigateNotification()
        initCalendar()
    }

    private fun onDateClick(calendarItem: CalendarItem) {
        // 한 주로 밀어올리는 코드 -> 바텀시트 때 연동 예정입니다.
//        calendarItem.date.getOnlySelectedWeek(calendarAdapter)

    }

    private fun initCalendar(){
        binding.apply {
            sunDay = getString(R.string.schedule_sun_day)
            monDay = getString(R.string.schedule_mon_day)
            tuesDay = getString(R.string.schedule_tues_day)
            wednsDay = getString(R.string.schedule_wedns_day)
            thursDay = getString(R.string.schedule_thurs_day)
            friDay = getString(R.string.schedule_fri_day)
            saturDay = getString(R.string.schedule_satur_day)

            rvCalendar.apply {
                itemAnimator = null
                adapter = calendarAdapter
                updateCalendar(Calendar.CURRENT)
            }
            ivPrevMonth.setOnClickListener {
                updateCalendar(Calendar.PREVIOUS)
            }
            ivNextMonth.setOnClickListener {
                updateCalendar(Calendar.NEXT)
            }
        }
    }

    private fun updateCalendar(state: Calendar) {
        when (state) {
            Calendar.CURRENT -> {}
            Calendar.NEXT -> {
                selectedDate = selectedDate.plusMonths(1)
            }

            Calendar.PREVIOUS -> {
                selectedDate = selectedDate.minusMonths(1)
            }
        }
        val (year, month) = selectedDate.format(DateTimeFormatter.ofPattern("yyyy년 M월")).split(" ")
        binding.tvYear.text = year
        binding.tvMonth.text = month
        calendarAdapter.submitList(selectedDate.createCalendar())
    }


    /**
     * 클릭한 아이템의 상태를 변경하고 나머지 아이템들의 상태를 초기화하는 함수
     * @param calendarAdapter : 업데이트할 어댑터
     * @param clickedItem : 클릭된 CalendarItem
     */
    private fun handleItemClick(
        clickedItem: CalendarItem,
    ) {
        val updatedList = calendarAdapter.currentList.map { item ->
            if (item.date == clickedItem.date) {
                item.copy(isSelected = true)
            } else {
                item.copy(isSelected = false)
            }
        }

        calendarAdapter.submitList(updatedList)
    }

    private fun navigateNotification() {
        binding.ivNoti.setOnClickListener {
            navigateDestination(
                findNavController(),
                R.id.action_fragment_schedule_to_fragment_notification
            )
        }
    }
}