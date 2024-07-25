package com.presentation.component.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class DividerItemDecoration(
    val context: Context,
    resId: Int,
) : RecyclerView.ItemDecoration() {

    private var mDivider: Drawable? = null

    init {
        mDivider = ContextCompat.getDrawable(context, resId)
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {


        val left = 0
        val right = parent.width
        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + (mDivider?.intrinsicHeight ?: 0)
            mDivider?.let {
                it.setBounds(left, top, right, bottom)
                it.draw(c)
            }

        }

    }
}