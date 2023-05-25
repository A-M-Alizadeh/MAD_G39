package com.example.profilelab.view_models

data class FireCourt(
    val id: String,
    val name: String,
    val location: HashMap<String, Double>,
    val sports: HashMap<String, String>,
    val address: String,
    val rate: Double,
) {
    override fun toString(): String {
        return name
    }
}