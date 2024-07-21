package com.presentation.util

enum class Ticket(val state: Int){
    ISSUED(0),
    UNISSUED(1)
}
enum class Calendar{
    CURRENT,
    NEXT,
    PREVIOUS,
    CHANGE
}