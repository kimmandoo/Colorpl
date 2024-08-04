package com.presentation.component.adapter.reservation.process

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.colorpl.presentation.databinding.ItemDateTableBinding
import com.domain.model.DateTableItem
import com.presentation.base.BaseDiffUtil

class ReservationDateTableAdapter(private val onItemClickListener: (DateTableItem) -> Unit) :
    ListAdapter<DateTableItem, ViewHolder>(BaseDiffUtil<DateTableItem>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDateTableBinding.inflate(inflater, parent, false)
        return ReservationDateTableViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ReservationDateTableViewHolder).bind(getItem(position))
    }

}