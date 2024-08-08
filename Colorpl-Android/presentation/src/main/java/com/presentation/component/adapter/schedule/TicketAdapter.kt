package com.presentation.component.adapter.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.colorpl.presentation.databinding.ItemTicketBinding
import com.domain.model.TicketResponse
import com.presentation.base.BaseDiffUtil

class TicketAdapter(private val onTicketClickListener: (TicketResponse) -> Unit) :
    ListAdapter<TicketResponse, ViewHolder>(BaseDiffUtil<TicketResponse>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTicketBinding.inflate(layoutInflater, parent, false)
        return TicketViewHolder(binding, onTicketClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is TicketViewHolder) holder.bind(getItem(position))
    }

}