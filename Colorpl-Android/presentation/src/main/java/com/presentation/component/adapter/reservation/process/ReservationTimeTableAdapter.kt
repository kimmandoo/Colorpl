package com.presentation.component.adapter.reservation.process

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.colorpl.presentation.databinding.ItemReservationTimeTableBinding
import com.domain.model.TimeTable
import com.presentation.base.BaseDiffUtil

class ReservationTimeTableAdapter(
    private val onClickListener: (TimeTable) -> Unit,
) : ListAdapter<TimeTable, ReservationTimeTableViewHolder>(
    BaseDiffUtil<TimeTable>()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReservationTimeTableViewHolder {
        val binding = ItemReservationTimeTableBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReservationTimeTableViewHolder(binding, onClickListener)
    }

    override fun onBindViewHolder(holder: ReservationTimeTableViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}