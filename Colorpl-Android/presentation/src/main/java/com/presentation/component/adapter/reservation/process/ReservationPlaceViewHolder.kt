package com.presentation.component.adapter.reservation.process

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.colorpl.presentation.databinding.ItemReservationPlaceBinding
import com.domain.model.ReservationPairInfo
import com.domain.model.TimeTable
import com.presentation.util.ViewPagerManager
import timber.log.Timber

class ReservationPlaceViewHolder(
    private val binding: ItemReservationPlaceBinding,
    private val timeTableClickListener: OnTimeTableClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: ReservationPairInfo, type: () -> Boolean) {
        binding.apply {
            Timber.d("데이터 확인 $data")
            this.type = type()
            this.reservationPairInfo = data

            val reservationTimeTableAdapter = ReservationTimeTableAdapter(onClickListener = {timeTable ->
                Toast.makeText(binding.root.context, "${data.placeName} ${data.theaterName} ${timeTable.startTime}", Toast.LENGTH_SHORT).show()
                timeTableClickListener.onTimeTableClick(data, timeTable)
            })
            rvTheater.adapter = reservationTimeTableAdapter
            reservationTimeTableAdapter.submitList(data.timeTableList)
        }
    }
}

interface OnTimeTableClickListener {
    fun onTimeTableClick(data: ReservationPairInfo, timeTable: TimeTable)
}
