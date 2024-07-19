package com.presentation.component.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.ItemNotificationBinding
import com.domain.model.Notification
import com.presentation.base.BaseDiffUtil
import com.presentation.component.custom.ItemTouchHelperListener

class NotificationAdapter : ListAdapter<Notification, NotificationAdapter.NotificationViewHolder>(
    BaseDiffUtil<Notification>()
), ItemTouchHelperListener {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NotificationViewHolder(
        val binding: ItemNotificationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: Notification) {
            binding.apply {
                Glide.with(this.ivEmoji)
                    .load(R.drawable.ic_emoji_think)
                    .into(this.ivEmoji)

                notificationData = notification
            }
        }
    }

    override fun onItemSwipe(position: Int) {
        notifyItemRemoved(position)
    }
}
