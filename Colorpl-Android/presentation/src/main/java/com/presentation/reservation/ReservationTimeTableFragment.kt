package com.presentation.reservation

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentReservationTimeTableBinding
import com.domain.model.DateTableItem
import com.domain.model.ReservationPairInfo
import com.domain.model.ReservationPlace
import com.domain.model.Theater
import com.domain.model.TimeTable
import com.domain.model.toModel
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.reservation.process.OnTimeTableClickListener
import com.presentation.component.adapter.reservation.process.ReservationDateTableAdapter
import com.presentation.component.adapter.reservation.process.ReservationPlaceAdapter
import com.presentation.viewmodel.ReservationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.time.LocalDate

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
            onDateTableClick(dateItem)
            viewModel.setReservationDate(dateItem.date)
        }
    }

    private fun onDateTableClick(dateTable: DateTableItem) {
        Toast.makeText(requireContext(), "${dateTable.date}", Toast.LENGTH_SHORT).show()
    }

    override fun initView() {
        initAdapter()
        initDateAdapter()
        initViewModel()
        observedSelectedDate()
    }


    private fun initAdapter() {
        binding.rcReservationTimeTable.adapter = reservationPlaceAdapter
        val data = listOf(
            ReservationPlace.DEFAULT,
            ReservationPlace.DEFAULT.copy(
                placeName = "병점CGV",
                theaterList = listOf(
                    Theater(
                        theaterId = 1,
                        theaterName = "1관",
                        theaterTotalSeatCount = 100,
                        timeTableList = listOf(
                            TimeTable(
                                timeTableId = 1,
                                startTime = "10:00",
                                endTime = "12:00",
                                remainingSeatCount = 99
                            ),
                            TimeTable(
                                timeTableId = 1,
                                startTime = "12:30",
                                endTime = "14:30",
                                remainingSeatCount = 88
                            ),
                            TimeTable(
                                timeTableId = 1,
                                startTime = "15:00",
                                endTime = "17:00",
                                remainingSeatCount = 77
                            ),
                            TimeTable(
                                timeTableId = 1,
                                startTime = "17:30",
                                endTime = "19:30",
                                remainingSeatCount = 15
                            ),
                            TimeTable(
                                timeTableId = 1,
                                startTime = "20:00",
                                endTime = "22:00",
                                remainingSeatCount = 23
                            )
                        )
                    ),
                )

            ),
            ReservationPlace.DEFAULT.copy(placeName = "오산CGV"),
        )


        reservationPlaceAdapter.submitList(
            data.toModel()
        )
    }

    private fun initDateAdapter() {
        binding.rcReservationDateTable.itemAnimator = null
        binding.rcReservationDateTable.adapter = reservationDateTableAdapter
        val today = LocalDate.now()
        val dateList = (0 until 14).map { i ->
            val date = today.plusDays(i.toLong())
            DateTableItem(
                date = date,
                isSelected = i == 0,
                isEvent = !date.dayOfMonth.toString().contains('6')
            )
        }

        reservationDateTableAdapter.submitList(dateList)
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
                    DateTableItem(
                        date = date,
                        isSelected = viewModel.reservationDate.value == date,
                        isEvent = !date.dayOfMonth.toString().contains('6')
                    )
                }
                reservationDateTableAdapter.submitList(dateList)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onTimeTableClick(data: ReservationPairInfo, timeTable: TimeTable) {
        viewModel.setReservationPlace(data.placeName)
        viewModel.setReservationTheater(data.theaterName)
        viewModel.setReservationTimeTable(timeTable)
        Timber.d("선택된 장소 : ${data.placeName}")
        Timber.d("선택된 상영관 : ${data.theaterName}")
        Timber.d("선택된 시간표 : ${timeTable.startTime}")
    }


}
