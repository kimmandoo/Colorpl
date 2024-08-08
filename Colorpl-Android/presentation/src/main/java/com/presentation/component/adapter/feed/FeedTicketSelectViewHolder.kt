package com.presentation.component.adapter.feed

import androidx.recyclerview.widget.RecyclerView
import com.colorpl.presentation.databinding.ItemFeedTicketSelectBinding
import com.domain.model.TicketResponse
import com.presentation.util.setImageCenterCrop

class FeedTicketSelectViewHolder(
    private val binding: ItemFeedTicketSelectBinding,
    private val onTicketClicked: (TicketResponse) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: TicketResponse) {
        binding.apply {
            ivPoster.setImageCenterCrop(data.imgUrl)
            tvTitle.text = data.name
            tvDate.text = data.dateTime
            tvLocation.text = data.location

            itemView.setOnClickListener {
                onTicketClicked(data)
            }
        }
    }
}