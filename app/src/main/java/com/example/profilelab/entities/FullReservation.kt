package com.example.profilelab.entities

data class FullReservation (
    val id: Int?,
    val user_id: Int,
    val court_sports_id: Int,
    val time_slot_id: Int,
    val status: Boolean,
    val name: String,
    val title: String,
    val start_time: String,
    val end_time: String,
    val description: String,
    val date_: String
        )