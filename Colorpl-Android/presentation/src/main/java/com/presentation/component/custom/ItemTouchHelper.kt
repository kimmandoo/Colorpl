package com.presentation.component.custom

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


interface ItemTouchHelperListener {
    fun onItemSwipe(position : Int)
    
}


class ItemTouchHelperCallback(val listener: ItemTouchHelperListener)
    : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

        return makeMovementFlags(0, swipeFlags)
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onItemSwipe(viewHolder.absoluteAdapterPosition)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }
}