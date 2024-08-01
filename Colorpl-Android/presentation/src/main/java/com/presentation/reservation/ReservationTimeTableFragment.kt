package com.presentation.reservation

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentReservationTimeTableBinding
import com.domain.model.ReservationPairInfo
import com.domain.model.ReservationPlace
import com.domain.model.Theater
import com.domain.model.TimeTable
import com.domain.model.toModel
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.reservation.process.OnTimeTableClickListener
import com.presentation.component.adapter.reservation.process.ReservationPlaceAdapter
import com.presentation.viewmodel.ReservationViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ReservationTimeTableFragment :
    BaseFragment<FragmentReservationTimeTableBinding>(R.layout.fragment_reservation_time_table),
    OnTimeTableClickListener {
    private val viewModel: ReservationViewModel by viewModels({ requireParentFragment() })
    private lateinit var reservationPlaceAdapter: ReservationPlaceAdapter
    private val selectedTimeTable = mutableListOf<ReservationPairInfo>()
    override fun initView() {
        initAdapter()
    }


    private fun initAdapter() {
        reservationPlaceAdapter = ReservationPlaceAdapter(this)
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

    override fun onTimeTableClick(data: ReservationPairInfo, timeTable: TimeTable) {
        viewModel.setReservationPlace(data.placeName)
        viewModel.setReservationTheater(data.theaterName)
        viewModel.setReservationTimeTable(timeTable)
        Timber.d("선택된 장소 : ${data.placeName}")
        Timber.d("선택된 상영관 : ${data.theaterName}")
        Timber.d("선택된 시간표 : ${timeTable.startTime}")
    }


}
