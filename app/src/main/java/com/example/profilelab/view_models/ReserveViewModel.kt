package com.example.profilelab.view_models

data class ReserveViewModel(
    val id: Int,
    val Court: String,
    val sport: String,
    val start: String,
    val end: String,
    val date: String,
    val status: Boolean,
    val description: String = "No description"
    ) {}
