package com.presentation.component.adapter.reservation.process

import androidx.recyclerview.widget.RecyclerView
import com.colorpl.presentation.databinding.ItemReservationTimeTableBinding
import com.domain.model.TimeTable
import timber.log.Timber

class ReservationTimeTableViewHolder(
    val binding : ItemReservationTimeTableBinding,
    val onClickListener : (TimeTable) -> Unit,
) : RecyclerView.ViewHolder(binding.root){
    fun bind(data : TimeTable){
        Timber.d("data : $data")
        binding.apply {
            timeTable = data
            clTime.setOnClickListener {
                onClickListener(data)
            }

        }
    }
}