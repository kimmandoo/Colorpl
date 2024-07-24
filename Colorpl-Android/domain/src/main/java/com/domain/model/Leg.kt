package com.domain.model

data class Leg(
    val distance: Int,
    val mode: String,
    val sectionTime: Int,
    val startName: String,
    val endName: String,
    val passShape: String?,
    val steps: List<Step>?
)
