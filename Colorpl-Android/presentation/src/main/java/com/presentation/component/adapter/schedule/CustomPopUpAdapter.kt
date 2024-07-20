package com.presentation.component.adapter.schedule

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.ItemPopUpBinding
import com.presentation.util.Ticket

class CustomPopupAdapter(private val items: List<String>) : BaseAdapter() {
    override fun getCount(): Int = items.size
    override fun getItem(position: Int): Any = items[position]
    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPopUpBinding.inflate(
            layoutInflater, parent,
            false
        )
        when (position) {
            Ticket.ISSUED.state -> {
                binding.ivIcon.setImageResource(R.drawable.ic_issued_ticket)
            }

            Ticket.UNISSUED.state -> {
                binding.ivIcon.setImageResource(R.drawable.ic_unissued_ticket)
            }
        }

        binding.tvTitle.text = items[position]
        binding.executePendingBindings()
        return binding.root
    }
}