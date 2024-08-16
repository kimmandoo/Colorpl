package com.presentation.component.adapter.mypage

import com.colorpl.presentation.*
import androidx.recyclerview.widget.RecyclerView
import com.colorpl.presentation.databinding.ItemNoticeBinding

class NoticeViewHolder(
    val binding: ItemNoticeBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind() {
        binding.apply {
            content = binding.root.context.getString(R.string.my_page_notice_content)
        }
    }
}