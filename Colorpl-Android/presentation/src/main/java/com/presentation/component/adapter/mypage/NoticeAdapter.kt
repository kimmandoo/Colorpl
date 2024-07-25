package com.presentation.component.adapter.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.colorpl.presentation.databinding.ItemNoticeBinding
import com.presentation.base.BaseDiffUtil

class NoticeAdapter : ListAdapter<String, NoticeViewHolder>(
    BaseDiffUtil<String>()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val binding = ItemNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoticeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bind()
    }
}