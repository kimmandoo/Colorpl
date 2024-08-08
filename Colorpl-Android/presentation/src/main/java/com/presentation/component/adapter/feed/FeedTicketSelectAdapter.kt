package com.presentation.component.adapter.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.colorpl.presentation.databinding.ItemFeedTicketSelectBinding
import com.domain.model.TicketResponse
import com.presentation.base.BaseDiffUtil

class FeedTicketSelectAdapter(private val onTicketClicked: (TicketResponse) -> Unit) :
    ListAdapter<TicketResponse, FeedTicketSelectViewHolder>(
        BaseDiffUtil<TicketResponse>()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedTicketSelectViewHolder {
        val binding =
            ItemFeedTicketSelectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedTicketSelectViewHolder(binding, onTicketClicked)
    }

    override fun onBindViewHolder(holder: FeedTicketSelectViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}