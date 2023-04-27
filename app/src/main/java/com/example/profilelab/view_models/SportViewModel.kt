package com.example.profilelab.view_models

data class SportViewModel(
    val id: Int,
    val title: String,
) {
    override fun toString(): String {
        return title
    }
}