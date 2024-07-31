package com.presentation.component.adapter.reservation.process


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.colorpl.presentation.databinding.ItemReservationPlaceBinding

import com.domain.model.ReservationPairInfo
import com.presentation.base.BaseDiffUtil
import com.presentation.util.ViewPagerManager
import timber.log.Timber

class ReservationPlaceAdapter :
    ListAdapter<ReservationPairInfo, ReservationPlaceAdapter.ReservationPlaceViewHolder>(
        BaseDiffUtil<ReservationPairInfo>()
    ) {
    private var location = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationPlaceViewHolder {
        val binding =
            ItemReservationPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservationPlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReservationPlaceViewHolder, position: Int) {

        holder.bind(getItem(position)) {
            val result = location == getItem(position).placeName
            location = getItem(position).placeName
            result
        }
    }

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
}
