package com.presentation.schedule

import android.util.Log
import android.widget.EdgeEffect
import android.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentScheduleBinding
import com.domain.model.CalendarItem
import com.domain.model.Ticket
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.schedule.CalendarAdapter
import com.presentation.component.adapter.schedule.CustomPopupAdapter
import com.presentation.component.adapter.schedule.TicketAdapter
import com.presentation.component.dialog.CalendarDialog
import com.presentation.util.Calendar
import com.presentation.util.TicketState
import com.presentation.util.createCalendar
import com.presentation.viewmodel.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

private const val TAG = "ScheduleFragment"

@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentScheduleBinding>(R.layout.fragment_schedule) {

    private val viewModel: ScheduleViewModel by viewModels()
    private lateinit var currentDate: CalendarItem
    private var handlePullState = 0
    private val calendarAdapter by lazy {
        CalendarAdapter(onItemClick = { calendarItem ->
            currentDate = calendarItem
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
    private val ticketAdapter by lazy {
        TicketAdapter(
            onTicketClickListener = {

            }
        )
    }
    private var selectedDate = LocalDate.now()

    override fun initView() {
        navigateNotification()
        initCalendar()
        initFAB()
        initTicketView()
    }

    private fun initCalendar() {
        binding.apply {
            val yearView = listOf(
                tvYear, ivYear
            )
            yearView.forEach { view ->
                view.setOnClickListener {
                    val dialog = CalendarDialog(requireContext()) { year, month ->
                        if (year == 0.toLong() && month == 0.toLong()) {
                            updateCalendar(state = Calendar.CURRENT)
                        } else {
                            updateCalendar(state = Calendar.CHANGE, month = month + 1, year = year)
                        }
                    }
                    dialog.show()
                }
            }

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

    private fun initTicketView() {
        with(binding) {
            rvTicket.adapter = ticketAdapter
            rvTicket.edgeEffectFactory = object : RecyclerView.EdgeEffectFactory() {
                override fun createEdgeEffect(
                    recyclerView: RecyclerView,
                    direction: Int
                ): EdgeEffect {
                    return object : EdgeEffect(recyclerView.context) {
                        override fun onPull(deltaDistance: Float) {
                            super.onPull(deltaDistance)
                            handlePull(direction, deltaDistance)
                        }

                        override fun onPull(deltaDistance: Float, displacement: Float) {
                            super.onPull(deltaDistance, displacement)
                            handlePull(direction, deltaDistance)
                        }
                    }
                }
            }
            ticketAdapter.submitList(
                listOf( // testcode
                    Ticket(
                        ticketId = 4706,
                        name = "Elijah Merritt",
                        date = Date(),
                        space = "ignota",
                        seat = "commune"
                    ),
                    Ticket(
                        ticketId = 4706,
                        name = "Elijah Merritt",
                        date = Date(),
                        space = "ignota",
                        seat = "commune"
                    ),
                    Ticket(
                        ticketId = 4706,
                        name = "Elijah Merritt",
                        date = Date(),
                        space = "ignota",
                        seat = "commune"
                    ),
                    Ticket(
                        ticketId = 4706,
                        name = "Elijah Merritt",
                        date = Date(),
                        space = "ignota",
                        seat = "commune"
                    )
                )
            )
        }
    }

    private fun handlePull(direction: Int, deltaDistance: Float) {
        Log.d(TAG, "deltaDistance: $deltaDistance")
        Log.d(TAG, "handlePull: $handlePullState")
        when (direction) {
            1 -> {
                handlePullState++
            }
        }
        if (handlePullState > 5) {
            updateCalendar(Calendar.RESTORE)
            handlePullState = 0
        }
    }

    /**
     * 선택한 날의 주차 시작점 찾기
     */
    private fun findStartOfWeek(): LocalDate {
        var current = currentDate.date
        while (current.dayOfWeek != DayOfWeek.SUNDAY) {
            current = current.minusDays(1)
        }
        return current
    }

    /**
     * 시작점 찾아와서 그 주를 가져오기
     * @param calendarAdapter : 업데이트 할 어댑터
     */
    private fun getOnlySelectedWeek(): List<CalendarItem> {
        val startOfWeek = findStartOfWeek()
        val endOfWeek = startOfWeek.plusDays(6)

        val updatedList = calendarAdapter.currentList.filter { item ->
            item.date >= startOfWeek && item.date <= endOfWeek
        }
        return updatedList
    }

    private fun updateCalendar(state: Calendar, month: Long = 0, year: Long = 0) {
        when (state) {
            Calendar.CURRENT -> {
                selectedDate = LocalDate.now()
            }

            Calendar.NEXT -> {
                selectedDate = selectedDate.plusMonths(1)
            }

            Calendar.PREVIOUS -> {
                selectedDate = selectedDate.minusMonths(1)
            }

            Calendar.CHANGE -> {
                val changedYear = year - selectedDate.year
                val changedMonth = month - selectedDate.monthValue

                selectedDate = selectedDate.plusYears(changedYear).plusMonths(changedMonth)
            }

            Calendar.RESTORE -> {
                selectedDate = currentDate.date
            }
        }
        val (currentYear, currentMonth) = selectedDate.format(DateTimeFormatter.ofPattern("yyyy년 M월"))
            .split(" ")
        binding.tvYear.text = currentYear
        binding.tvMonth.text = currentMonth
        if (state == Calendar.RESTORE) {
            calendarAdapter.submitList(selectedDate.createCalendar().map { item ->
                if (item.date == currentDate.date) {
                    item.copy(isSelected = true)
                } else {
                    item.copy(isSelected = false)
                }
            })
        } else {
            calendarAdapter.submitList(selectedDate.createCalendar())
        }
    }


    /**
     * 클릭한 아이템의 상태를 변경하고 나머지 아이템들의 상태를 초기화하는 함수
     * @param calendarAdapter : 업데이트할 어댑터
     * @param clickedItem : 클릭된 CalendarItem
     */
    private fun handleItemClick(
        clickedItem: CalendarItem,
    ) {

        val updatedList = getOnlySelectedWeek().map { item ->
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
            animationStyle = R.style.FABPopupAnimation
            setBackgroundDrawable(
                ContextCompat.getDrawable(
                    binding.root.context,
                    android.R.color.transparent
                )
            )
            horizontalOffset = -binding.fabAddTicket.width / 2
            verticalOffset = -binding.fabAddTicket.height / 2
            setAdapter(popUpAdapter)
            anchorView = binding.fabAddTicket
            width =
                binding.root.context.resources.getDimensionPixelSize(R.dimen.default_popup_width)
            height = ListPopupWindow.WRAP_CONTENT
            isModal = true
            setOnItemClickListener { _, _, position, _ ->
                when (position) {
                    TicketState.ISSUED.state -> {
                        Timber.tag("fab").d("issued")
                    }

                    TicketState.UNISSUED.state -> {
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