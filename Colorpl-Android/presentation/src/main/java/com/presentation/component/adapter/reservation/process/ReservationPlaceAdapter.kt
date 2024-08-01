package com.presentation.component.adapter.reservation.process


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.colorpl.presentation.databinding.ItemReservationPlaceBinding

import com.domain.model.ReservationPairInfo
import com.presentation.base.BaseDiffUtil

class ReservationPlaceAdapter (
    private val clickListener: OnTimeTableClickListener
):
    ListAdapter<ReservationPairInfo, ReservationPlaceViewHolder>(
        BaseDiffUtil<ReservationPairInfo>()
    ) {
    private var location = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationPlaceViewHolder {
        val binding =
            ItemReservationPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservationPlaceViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ReservationPlaceViewHolder, position: Int) {

        holder.bind(getItem(position)) {
            val result = location == getItem(position).placeName
            location = getItem(position).placeName
            result
        }
    }


}
