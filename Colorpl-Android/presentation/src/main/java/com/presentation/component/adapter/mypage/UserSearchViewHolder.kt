package com.presentation.component.adapter.mypage

import androidx.recyclerview.widget.RecyclerView
import com.colorpl.presentation.databinding.ItemUserSearchBinding
import com.domain.model.MemberSearch

import com.presentation.util.setImageCircleCrop


class UserSearchViewHolder(
    val binding: ItemUserSearchBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: MemberSearch) {
        binding.apply {
            memberSearch = data
            ivProfileImg.setImageCircleCrop(data.profileImage, true)
        }
    }
}
