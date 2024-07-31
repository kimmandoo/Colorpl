package com.presentation.util

import android.widget.EdgeEffect
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.overScrollControl(logic: (Int, Float)-> Unit){
    edgeEffectFactory = object : RecyclerView.EdgeEffectFactory() {
        override fun createEdgeEffect(
            recyclerView: RecyclerView,
            direction: Int
        ): EdgeEffect {
            return object : EdgeEffect(recyclerView.context) {
                override fun onPull(deltaDistance: Float) {
                    super.onPull(deltaDistance)
                    logic(direction, deltaDistance)
                }

                override fun onPull(deltaDistance: Float, displacement: Float) {
                    super.onPull(deltaDistance, displacement)
                    logic(direction, deltaDistance)
                }
            }
        }
    }
}

