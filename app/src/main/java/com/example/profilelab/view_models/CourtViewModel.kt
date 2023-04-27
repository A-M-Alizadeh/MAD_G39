package com.example.profilelab.view_models

data class CourtViewModel(
    val id: Int,
    val name: String,
) {
    override fun toString(): String {
        return name
    }
}