package com.presentation.component.adapter.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.colorpl.presentation.databinding.ItemPopUpBinding

class CustomPopupAdapter(private val items: List<String>) : BaseAdapter() {
    override fun getCount(): Int = items.size
    override fun getItem(position: Int): Any = items[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: FABViewHolder = if (convertView == null) {
            FABViewHolder(
                ItemPopUpBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            convertView.tag as FABViewHolder
        }

        return holder.bind(items[position], position)
    }
}