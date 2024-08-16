package com.presentation.component.adapter.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.colorpl.presentation.databinding.ItemFeedFilterBinding
import com.domain.model.FilterItem
import com.presentation.base.BaseDiffUtil

class FilterAdapter(private val onItemClickListener: (FilterItem) -> Unit): ListAdapter<FilterItem, ViewHolder>(BaseDiffUtil<FilterItem>()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFeedFilterBinding.inflate(layoutInflater, parent, false)
        return FilterViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(holder is FilterViewHolder) holder.bind(getItem(position))
    }
}