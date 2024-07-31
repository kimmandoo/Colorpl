package com.presentation.reservation

import androidx.recyclerview.widget.GridLayoutManager
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentReservationTimeTableBinding
import com.domain.model.ReservationPlace
import com.domain.model.Theater
import com.domain.model.TimeTable
import com.domain.model.toModel
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.reservation.process.ReservationPlaceAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ReservationTimeTableFragment :
    BaseFragment<FragmentReservationTimeTableBinding>(R.layout.fragment_reservation_time_table) {

    private lateinit var reservationPlaceAdapter: ReservationPlaceAdapter

    override fun initView() {
        initAdapter()
    }


    private fun initAdapter() {
        reservationPlaceAdapter = ReservationPlaceAdapter()
        binding.rcReservationTimeTable.adapter = reservationPlaceAdapter
        val data = listOf(
            ReservationPlace.DEFAULT,
            ReservationPlace.DEFAULT.copy(placeName = "병점CGV",
                theaterList = listOf(  Theater(
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
                ),)

                ),
            ReservationPlace.DEFAULT.copy(placeName = "오산CGV"),
        )


        reservationPlaceAdapter.submitList(
            data.toModel()
        )
    }


}
