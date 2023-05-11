package com.example.profilelab.view_models

data class TimeSlotViewModel(
    val id: String,
    val startTime: String,
    val endTime: String,
    var checked:Boolean = false
){
    override fun toString(): String {
        return "$startTime - $endTime"
    }
}