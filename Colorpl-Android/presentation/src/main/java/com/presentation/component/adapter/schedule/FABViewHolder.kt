package com.presentation.component.adapter.schedule

import android.view.View
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.ItemPopUpBinding
import com.presentation.util.TicketState

class FABViewHolder(private val binding: ItemPopUpBinding) {
    private val itemView: View = binding.root

    init {
        itemView.tag = this
    }

    fun bind(item: String): View {
        if (item.isEmpty()) {
            itemView.visibility = View.INVISIBLE
        }
        binding.tvTitle.text = item
        binding.ivIcon.setImageResource(R.drawable.ic_issued_ticket)
        binding.executePendingBindings()
        return itemView
    }
}