package com.example.profilelab.view_models

data class TimeSlotViewModel(
    val id: Int,
    val startTime: String,
    val endTime: String,
    val available: Boolean = true
){
    override fun toString(): String {
        return "$startTime - $endTime"
    }
}