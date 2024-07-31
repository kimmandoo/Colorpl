package com.presentation.component.adapter.reservation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.colorpl.presentation.databinding.ItemSeatBinding
import com.domain.model.Seat
import com.presentation.base.BaseDiffUtil

class SeatAdapter(
    private val onSeatSelected: (Seat) -> Unit
) : ListAdapter<Seat, RecyclerView.ViewHolder>(BaseDiffUtil<Seat>()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSeatBinding.inflate(layoutInflater, parent, false)
        return SeatViewHolder(binding, onSeatSelected)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SeatViewHolder).bind(seat = getItem(position))
    }
}