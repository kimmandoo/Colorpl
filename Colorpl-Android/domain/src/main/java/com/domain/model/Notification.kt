package com.domain.model

data class Notification(
    val emoji : String,
    val type : Int,
    val content : String,
    val date : String
){


    companion object{
        val DEFAULT = listOf(Notification("",0,"",""),
            Notification("",0,"",""),
            Notification("",0,"",""),
            Notification("",0,"",""),
            Notification("",0,"",""),
            Notification("",0,"",""),
        )
    }
}
