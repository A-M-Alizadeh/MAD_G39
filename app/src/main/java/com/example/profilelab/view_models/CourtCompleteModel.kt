package com.example.profilelab.view_models

data class CourtCompleteModel(
    val id: Int,
    val name: String,
    val location: Map<String, String>,
    val sports: Map<Int, String>,
    )