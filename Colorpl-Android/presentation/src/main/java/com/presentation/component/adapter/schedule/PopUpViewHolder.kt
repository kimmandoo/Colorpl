package com.presentation.component.adapter.schedule

import android.view.View
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.ItemPopUpBinding
import com.presentation.util.Ticket

class PopUpViewHolder(private val binding: ItemPopUpBinding) {
    private val itemView: View = binding.root

    init {
        itemView.tag = this
    }

    fun bind(item: String, position: Int): View {
        binding.tvTitle.text = item
        when (position) {
            Ticket.ISSUED.state -> {
                binding.ivIcon.setImageResource(R.drawable.ic_issued_ticket)
            }

            Ticket.UNISSUED.state -> {
                binding.ivIcon.setImageResource(R.drawable.ic_unissued_ticket)
            }
        }
        binding.executePendingBindings()
        return itemView
    }
}