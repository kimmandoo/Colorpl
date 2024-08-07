package com.presentation.component.adapter.schedule

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.colorpl.presentation.databinding.ItemTicketBinding
import com.domain.model.TicketResponse

class TicketViewHolder(
    private val binding: ItemTicketBinding,
    private val onTicketClickListener: () -> Unit
) : ViewHolder(binding.root) {
    fun bind(data: TicketResponse) {
        binding.apply {
            Glide.with(itemView.context).load(data.imgUrl).centerCrop().into(ivTicket)
            tvTitle.text = data.name
            tvWhere.text = data.location
            tvWhen.text = data.dateTime
        }
        itemView.setOnClickListener {
            onTicketClickListener()
        }
    }
}