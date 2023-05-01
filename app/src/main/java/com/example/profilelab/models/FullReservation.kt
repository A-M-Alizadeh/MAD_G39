package com.example.profilelab.models

data class FullReservation (
    val reservation_id: Int?,
    val user_id: Int,
    val court_sports_id: Int,
    val time_slot_id: Int,
    val court: String,
    val sport: String,
    val status: Boolean,
    val name: String,
    val title: String,
    val start_time: String,
    val end_time: String,
    val description: String,
    val date_: String
    )