package com.example.profilelab.view_models

data class ReservationLiveModel(
    val id: String,
    val court: String,
    val sport : String,
    val date : String,
    val startTime : String,
    val endTime : String,
    val description : String,
    val isRated : Boolean,
    val comment : String,
    val rate : Int,

) {
    override fun toString(): String {
        return "ReservationLiveViewModel(id='$id', court='$court', sport='$sport', date='$date', startTime='$startTime', endTime='$endTime', description='$description', isRated=$isRated, comment='$comment', rate=$rate)"
    }
}