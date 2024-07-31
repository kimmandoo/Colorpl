package com.presentation.component.adapter.reservation.process

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.colorpl.presentation.databinding.ItemReservationPlaceBinding
import com.domain.model.ReservationPairInfo
import com.presentation.util.ViewPagerManager
import timber.log.Timber

class ReservationPlaceViewHolder(
    val binding: ItemReservationPlaceBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: ReservationPairInfo, type: () -> Boolean) {
        binding.apply {
            Timber.d("데이터 확인 $data")
            this.type = type()
            this.reservationPairInfo = data

            val reservationTimeTableAdapter = ReservationTimeTableAdapter(onClickListener = {timeTable ->
                Toast.makeText(binding.root.context, "${data.placeName} ${data.theaterName} ${timeTable.startTime}", Toast.LENGTH_SHORT).show()
                ViewPagerManager.moveNext()
            })
            rvTheater.adapter = reservationTimeTableAdapter
            reservationTimeTableAdapter.submitList(data.timeTableList)
        }
    }
}