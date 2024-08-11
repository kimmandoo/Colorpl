package com.presentation.schedule

import android.view.MotionEvent
import android.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentScheduleBinding
import com.domain.model.TicketResponse
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
import com.presentation.util.getPattern
import com.presentation.util.overScrollControl
import com.presentation.viewmodel.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


private const val TAG = "ScheduleFragment"

@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentScheduleBinding>(R.layout.fragment_schedule) {

    private val viewModel: ScheduleViewModel by viewModels()
    private var handlePullState = 0
    private val swipeThreshold: Int = 100
    private var startX: Float = 0f
    private var startY: Float = 0f
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
            onTicketClickListener = { ticket ->
                onTicketClickListener(ticket)
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

    private fun onTicketClickListener(ticket: TicketResponse) {
        val action = ScheduleFragmentDirections.actionFragmentScheduleToFragmentTicket(ticket)
        findNavController().navigate(action)
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.calendarItems.collectLatest { calendarList ->
                    calendarAdapter.submitList(
                        viewModel.matchTicketsToCalendar(
                            calendarList,
                            viewModel.tickets.value
                        )
                    )
                }
            }
            launch {
                viewModel.tickets.collectLatest { tickets ->
                    calendarAdapter.submitList(
                        viewModel.matchTicketsToCalendar(
                            viewModel.calendarItems.value,
                            tickets
                        )
                    )
                    ticketAdapter.submitList(tickets)
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
                viewModel.clickedDate.collectLatest { calendarItem ->
                    calendarItem?.let {
                        viewModel.getMonthlyTicket(it.date.getPattern("yyyy-MM-dd"))
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
                addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                        when (e.action) {
                            MotionEvent.ACTION_DOWN -> {
                                startX = e.x
                                startY = e.y
                            }

                            MotionEvent.ACTION_UP -> {
                                val endX = e.x
                                val endY = e.y
                                val distanceX = Math.abs(endX - startX)
                                val distanceY = Math.abs(endY - startY)

                                if (distanceX > swipeThreshold && distanceX > distanceY) {
                                    if (endX > startX) {
                                        viewModel.swipeUpdateCalendar(-1) // 오른쪽으로 스와이프
                                    } else {
                                        viewModel.swipeUpdateCalendar(1) // 왼쪽으로 스와이프
                                    }
                                    return true
                                }
                            }
                        }
                        return false
                    }
                })
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
                    TicketType.CAMERA_ISSUED -> {
                        ScheduleFragmentDirections.actionFragmentScheduleToNavTicketGraph(TicketType.CAMERA_ISSUED)
                    }

                    TicketType.GALLERY_ISSUED -> {
                        ScheduleFragmentDirections.actionFragmentScheduleToNavTicketGraph(TicketType.GALLERY_ISSUED)
                    }

                    TicketType.CAMERA_UNISSUED -> {
                        ScheduleFragmentDirections.actionFragmentScheduleToNavTicketGraph(TicketType.CAMERA_UNISSUED)
                    }

                    TicketType.GALLERY_UNISSUED -> {
                        ScheduleFragmentDirections.actionFragmentScheduleToNavTicketGraph(TicketType.GALLERY_UNISSUED)
                    }
                }
                navigateDestination(action)
            }
        )
        dialog.show()
    }
}