package com.presentation.schedule

import android.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentScheduleBinding
import com.domain.model.Ticket
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.schedule.CalendarAdapter
import com.presentation.component.adapter.schedule.CustomPopupAdapter
import com.presentation.component.adapter.schedule.TicketAdapter
import com.presentation.component.dialog.CalendarDialog
import com.presentation.component.dialog.TicketCreateDialog
import com.presentation.util.Calendar
import com.presentation.util.CalendarMode
import com.presentation.util.TicketState
import com.presentation.util.TicketType
import com.presentation.util.overScrollControl
import com.presentation.viewmodel.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date


private const val TAG = "ScheduleFragment"

@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentScheduleBinding>(R.layout.fragment_schedule) {

    private val viewModel: ScheduleViewModel by viewModels()
    private var handlePullState = 0
    private val calendarAdapter by lazy {
        CalendarAdapter(onItemClick = { calendarItem ->
            viewModel.setClickedDate(calendarItem)
            viewModel.handleItemClick(calendarItem)
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
                findNavController().navigate(R.id.fragment_ticket)
            }
        )
    }

    override fun initView() {
        observeViewModel()
        navigateNotification()
        initCalendar()
        initFAB()
        initTicketView()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.calendarItems.collectLatest { calendarList ->
                    calendarAdapter.submitList(calendarList)
                }
            }
            launch {
                viewModel.calendarMode.collectLatest { mode ->
                    when (mode) {
                        CalendarMode.MONTH -> {
                            setMonthMode()
                        }

                        CalendarMode.WEEK -> {
                            setWeekMode()
                        }
                    }
                }
            }
            launch {
                viewModel.displayDate.collect { date ->
                    binding.tvYear.text = date.split(" ").first()
                    binding.tvMonth.text = date.split(" ").drop(1).joinToString(" ")
                }
            }
        }
    }

    private fun initCalendar() {
        binding.apply {
            val yearView = listOf(
                tvYear, ivYear
            )
            yearView.forEach { view ->
                view.setOnClickListener {
                    showYearMonthDialog()
                }
            }

            rvCalendar.apply {
                adapter = calendarAdapter
                itemAnimator = null
            }
        }
    }

    private fun initTicketView() {
        binding.apply {
            rvTicket.adapter = ticketAdapter
            rvTicket.overScrollControl { direction, deltaDistance ->
                if (viewModel.clickedDate.value != null) {
                    handlePull(direction)
                }
            }
            ticketAdapter.submitList(
                listOf(
                    // testcode
                    Ticket(
                        ticketId = 4706,
                        name = "Elijah Merritt",
                        date = Date().toString(),
                        theater = "ignota",
                        seat = "commune",
                        category = "뮤지컬"
                    ),
                )
            )
        }
    }

    private fun handlePull(direction: Int) {
        when (direction) {
            1 -> {
                handlePullState++
            }
        }
        if (handlePullState > 5) {
            viewModel.updateCalendar(Calendar.RESTORE)
            handlePullState = 0
        }
    }

    private fun setWeekMode() {
        binding.ivPrevMonth.setOnClickListener {
            viewModel.updateCalendarWeekMode(Calendar.PREVIOUS)
        }
        binding.ivNextMonth.setOnClickListener {
            viewModel.updateCalendarWeekMode(Calendar.NEXT)
        }
    }

    private fun setMonthMode() {
        binding.ivPrevMonth.setOnClickListener {
            viewModel.updateCalendar(Calendar.PREVIOUS)
        }
        binding.ivNextMonth.setOnClickListener {
            viewModel.updateCalendar(Calendar.NEXT)
        }
    }

    private fun navigateNotification() {
        binding.ivNoti.setOnClickListener {
            navigateDestination(
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
                        showTicketCreateDialog(TicketState.ISSUED)
                    }

                    TicketState.UNISSUED.state -> {
                        showTicketCreateDialog(TicketState.UNISSUED)
                    }
                }

                listPopupWindow.dismiss()
            }
        }

        binding.fabAddTicket.setOnClickListener {
            listPopupWindow.show()
        }
    }

    private fun showYearMonthDialog() {
        val dialog = CalendarDialog(requireContext()) { year, month ->
            if (year == 0.toLong() && month == 0.toLong()) {
                viewModel.updateCalendar(state = Calendar.CURRENT)
            } else {
                viewModel.updateCalendar(state = Calendar.CHANGE, month = month + 1, year = year)
            }
        }
        dialog.show()
    }

    private fun showTicketCreateDialog(ticketState: TicketState) {
        val dialog = TicketCreateDialog(
            requireContext(),
            type = ticketState,
            action = { ticketType ->
                val action = when (ticketType) {
                    TicketType.CAMERA -> {
                        ScheduleFragmentDirections.actionFragmentScheduleToNavTicketGraph(TicketType.CAMERA)
                    }

                    TicketType.GALLERY -> {
                        ScheduleFragmentDirections.actionFragmentScheduleToNavTicketGraph(TicketType.GALLERY)
                    }
                }
                navigateDestination(action)
            }
        )
        dialog.show()
    }
}