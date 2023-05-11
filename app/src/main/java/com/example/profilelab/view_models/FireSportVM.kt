package com.example.profilelab.view_models

data class FireSportVM(
    val id: String,
    val name: String,
    ) {
        override fun toString(): String {
            return name
        }
}