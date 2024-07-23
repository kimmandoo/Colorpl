package com.presentation.component.adapter.schedule

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.colorpl.presentation.databinding.ItemTicketBinding
import com.domain.model.Ticket

class TicketViewHolder(
    private val binding: ItemTicketBinding,
    private val onTicketClickListener: () -> Unit
) : ViewHolder(binding.root) {
    fun bind(data: Ticket) {
        itemView.setOnClickListener {
            onTicketClickListener()
        }
    }
}