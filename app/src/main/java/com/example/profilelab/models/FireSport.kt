package com.example.profilelab.models

data class FireSport(
    val id: String,
    val name: String,
) {
    override fun toString(): String {
        return name
    }
}