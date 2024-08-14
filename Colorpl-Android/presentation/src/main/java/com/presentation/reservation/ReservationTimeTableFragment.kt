package com.presentation.reservation

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentReservationTimeTableBinding
import com.domain.model.DateTableItem
import com.domain.model.ReservationPairInfo
import com.domain.model.TimeTable
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.reservation.process.OnTimeTableClickListener
import com.presentation.component.adapter.reservation.process.ReservationDateTableAdapter
import com.presentation.component.adapter.reservation.process.ReservationPlaceAdapter
import com.presentation.util.ViewPagerManager
import com.presentation.viewmodel.ReservationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ReservationTimeTableFragment :
    BaseFragment<FragmentReservationTimeTableBinding>(R.layout.fragment_reservation_time_table),
    OnTimeTableClickListener {
    private val viewModel: ReservationViewModel by viewModels({ requireParentFragment() })
    private val reservationPlaceAdapter by lazy {
        ReservationPlaceAdapter(this)
    }
    private val reservationDateTableAdapter by lazy {
        ReservationDateTableAdapter { dateItem ->
            viewModel.setReservationDate(dateItem.date)
        }
    }

    override fun initView() {
        initTimeTableAdapter()
        initDateAdapter()
        initViewModel()
        observedSelectedDate()
        observedReservationSchedule()
        observeReservationSchedule()
        observedSelectedDate()
    }


    private fun initTimeTableAdapter() {
        binding.rcReservationTimeTable.adapter = reservationPlaceAdapter
    }

    private fun initDateAdapter() {
        binding.rcReservationDateTable.apply {
            this@apply.itemAnimator = null
            this@apply.adapter = reservationDateTableAdapter
        }
    }

    private fun initViewModel() {
        viewModel.setReservationDate(LocalDate.now())
        binding.apply {
            this@apply.viewModel = this@ReservationTimeTableFragment.viewModel
        }
    }

    private fun observedSelectedDate() {
        viewModel.reservationDate.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                val dateList = (0 until 14).map { i ->
                    val date = LocalDate.now().plusDays(i.toLong())
                    val formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                    Timber.tag("viewModel_Schedule").d(viewModel.reservationSchedule.value.toString())
                    DateTableItem(
                        date = date,
                        isSelected = viewModel.reservationDate.value == date,
                        isEvent = viewModel.reservationSchedule.value[formattedDate]?: false
                    )
                }
                reservationDateTableAdapter.submitList(dateList)

                val formattedDate = viewModel.reservationDate.value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                viewModel.getReservationSchedule(viewModel.reservationDetailId.value, formattedDate)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observedReservationSchedule() {
        viewModel.getReservationSchedule.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                reservationPlaceAdapter.submitList(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeReservationSchedule() {
        viewModel.reservationSchedule.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
//                observedSelectedDate()
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onTimeTableClick(data: ReservationPairInfo, timeTable: TimeTable) {
        with(viewModel) {
            setReservationPlace(data.placeName)
            setReservationTheater(data.hallName)
            setReservationTimeTable(timeTable)
            getReservationSeat(reservationDetailId.value, timeTable.scheduleId)
            Timber.tag("Seat API 요청").d("${reservationDetailId.value}, ${timeTable.scheduleId}")
        }
        ViewPagerManager.moveNext()
    }


}
