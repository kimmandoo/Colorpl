package com.presentation.schedule

import android.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
import com.presentation.util.getOnlySelectedWeek
import com.presentation.util.overScrollControl
import com.presentation.viewmodel.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Date
import java.util.Locale
import java.util.Locale.KOREA
import java.util.Locale.KOREAN


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
                adapter = calendarAdapter
                itemAnimator?.apply {
                    removeDuration = 0
                    moveDuration = 20
                }

                updateCalendar(Calendar.CURRENT)
            }
        }
    }

    private fun initTicketView() {
        binding.apply {
            rvTicket.adapter = ticketAdapter
            rvTicket.overScrollControl { direction, deltaDistance ->
                handlePull(direction, deltaDistance)
            }
            ticketAdapter.submitList(
                listOf( // testcode
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
        when (direction) {
            1 -> {
                handlePullState++
            }
        }
        if (handlePullState > 5) {
            if (::currentDate.isInitialized){
                setMonthMode()
                updateCalendar(Calendar.RESTORE)
            }
            handlePullState = 0
        }
    }

    private fun updateCalendarWeekMode(
        state: Calendar
    ) {
        selectedDate = when (state) {
            Calendar.CURRENT -> {
                LocalDate.now()
            }

            Calendar.NEXT -> {
                selectedDate.plusWeeks(1)
            }

            Calendar.PREVIOUS -> {
                selectedDate.plusWeeks(-1)
            }

            else -> {
                null
            }
        }
        val (currentYear, currentMonth) = selectedDate.format(
            DateTimeFormatter.ofPattern(
                "yyyy년 M월"
            )
        )
            .split(" ")
        val weekFields = WeekFields.of(Locale.getDefault())
        val weekOfMonth = selectedDate.get(weekFields.weekOfMonth())
        binding.tvYear.text = currentYear
        binding.tvMonth.text = buildString {
            append(currentMonth)
            append(" ")
            append(weekOfMonth)
            append("주")
        }

        calendarAdapter.submitList(selectedDate.getOnlySelectedWeek(selectedDate.createCalendar()).map { item ->
            if (item.date == currentDate.date) {
                item.copy(isSelected = true)
            } else {
                item.copy(isSelected = false)
            }
        })
    }

    private fun updateCalendar(
        state: Calendar,
        month: Long = 0,
        year: Long = 0,
    ) {
        selectedDate = when (state) {
            Calendar.CURRENT -> {
                LocalDate.now()
            }

            Calendar.NEXT -> {
                selectedDate.plusMonths(1)
            }

            Calendar.PREVIOUS -> {
                selectedDate.minusMonths(1)
            }

            Calendar.CHANGE -> {
                val changedYear = year - selectedDate.year
                val changedMonth = month - selectedDate.monthValue

                selectedDate.plusYears(changedYear).plusMonths(changedMonth)
            }

            Calendar.RESTORE -> {
                currentDate.date
            }
        }
        val (currentYear, currentMonth) = selectedDate.format(DateTimeFormatter.ofPattern("yyyy년 M월"))
            .split(" ")
        binding.tvYear.text = currentYear
        binding.tvMonth.text = currentMonth
        val updateList = if (state == Calendar.RESTORE) {
            selectedDate.createCalendar().map { item ->
                if (item.date == currentDate.date) {
                    item.copy(isSelected = true)
                } else {
                    item.copy(isSelected = false)
                }
            }
        } else {
            selectedDate.createCalendar()
        }
        calendarAdapter.submitList(updateList)
    }

    private fun setWeekMode(clickedItem: CalendarItem){
        binding.ivPrevMonth.setOnClickListener {
            updateCalendarWeekMode(Calendar.PREVIOUS)
        }
        binding.ivNextMonth.setOnClickListener {
            updateCalendarWeekMode(Calendar.NEXT)
        }
        val (currentYear, currentMonth) = clickedItem.date.format(
            DateTimeFormatter.ofPattern(
                "yyyy년 M월"
            )
        )
            .split(" ")
        val weekFields = WeekFields.of(Locale.getDefault())
        val weekOfMonth = clickedItem.date.get(weekFields.weekOfMonth())
        binding.tvYear.text = currentYear
        binding.tvMonth.text = buildString {
            append(currentMonth)
            append(" ")
            append(weekOfMonth)
            append("주")
        }
    }

    private fun setMonthMode(){
        binding.ivPrevMonth.setOnClickListener {
            updateCalendar(Calendar.PREVIOUS)
        }
        binding.ivNextMonth.setOnClickListener {
            updateCalendar(Calendar.NEXT)
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
        setWeekMode(clickedItem)
        val updatedList = clickedItem.date.getOnlySelectedWeek(calendarAdapter.currentList).map { item ->
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