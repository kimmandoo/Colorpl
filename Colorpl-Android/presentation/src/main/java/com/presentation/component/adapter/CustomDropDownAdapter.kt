package com.presentation.component.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.colorpl.presentation.databinding.ItemDropDownBinding
import com.presentation.base.BaseDiffUtil
import com.presentation.component.custom.DropDownData


class CustomDropDownAdapter :
    ListAdapter<DropDownData, CustomDropDownAdapter.CustomDropDownViewHolder>(
        BaseDiffUtil<DropDownData>()
    ) {

    private var onItemCLickListener: ((DropDownData) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomDropDownViewHolder {
        val binding =
            ItemDropDownBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomDropDownViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomDropDownViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onItemCLickListener?.let { it(getItem(holder.absoluteAdapterPosition)) }
        }
    }

    class CustomDropDownViewHolder(
        val binding: ItemDropDownBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dropDownData: DropDownData) {
            binding.apply {
                this.dropDownMenu = dropDownData
            }
        }
    }

    fun setItemClickListener(listener: (DropDownData) -> Unit) {
        this.onItemCLickListener = listener
    }
}