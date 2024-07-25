package com.presentation.component.adapter.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.colorpl.presentation.databinding.ItemUserSearchBinding
import com.presentation.base.BaseDiffUtil

class UserSearchAdapter : ListAdapter<String, UserSearchViewHolder >(
  BaseDiffUtil<String>()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchViewHolder {
        val binding = ItemUserSearchBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserSearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}