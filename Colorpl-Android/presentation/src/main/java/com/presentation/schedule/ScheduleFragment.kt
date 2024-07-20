package com.presentation.schedule

import android.widget.ListPopupWindow
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentScheduleBinding
import com.domain.model.CalendarItem
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.schedule.CalendarAdapter
import com.presentation.component.adapter.schedule.CustomPopupAdapter
import com.presentation.util.Calendar
import com.presentation.util.Ticket
import com.presentation.util.createCalendar
import com.presentation.viewmodel.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val TAG = "ScheduleFragment"

@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentScheduleBinding>(R.layout.fragment_schedule) {

    private val viewModel: ScheduleViewModel by viewModels()
    private val calendarAdapter by lazy {
        CalendarAdapter(onItemClick = { calendarItem ->
            onDateClick(calendarItem)
            handleItemClick(calendarItem)
        })
    }
    private val popUpAdapter by lazy {
        CustomPopupAdapter(
            listOf(
                getString(R.string.schedule_ticket_issued),
                getString(R.string.schedule_ticket_unissued)
            )
        )
    }
    private var selectedDate = LocalDate.now()

    override fun initView() {
        navigateNotification()
        initCalendar()
        initFAB()
    }

    private fun onDateClick(calendarItem: CalendarItem) {
        // 한 주로 밀어올리는 코드 -> 바텀시트 때 연동 예정입니다.
//        calendarItem.date.getOnlySelectedWeek(calendarAdapter)

    }

    private fun initCalendar() {
        binding.apply {
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

    private fun initFAB() {
        val listPopupWindow = ListPopupWindow(binding.root.context)
        listPopupWindow.apply {
            setBackgroundDrawable(
                ContextCompat.getDrawable(
                    binding.root.context,
                    android.R.color.transparent
                )
            )
            setAdapter(popUpAdapter)
            anchorView = binding.fabAddTicket
            width =
                binding.root.context.resources.getDimensionPixelSize(R.dimen.default_popup_width)
            height = ListPopupWindow.WRAP_CONTENT
            isModal = true
            setOnItemClickListener { _, _, position, _ ->
                when (position) {
                    Ticket.ISSUED.state -> {
                        Timber.tag("fab").d("issued")
                    }

                    Ticket.UNISSUED.state -> {
                        Timber.tag("fab").d("unissued")
                    }
                }
                listPopupWindow.dismiss()
            }
        }

        binding.fabAddTicket.setOnClickListener {
            listPopupWindow.show()
        }
    }
}