package com.presentation.component.adapter.feed

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.ItemFeedFilterBinding
import com.domain.model.FilterItem

class FilterViewHolder(
    private val binding: ItemFeedFilterBinding,
    private val onItemClick: (FilterItem) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(filter: FilterItem) {
        binding.apply {
            tvFilter.text = filter.name
            tvFilter.setTextColor(
                if (filter.isSelected) binding.root.resources.getColor(
                    R.color.imperial_red,
                    null
                ) else binding.root.resources.getColor(R.color.timber_wolf, null)
            )

            tvFilter.setOnClickListener {
                onItemClick(filter)
            }
        }
    }
}