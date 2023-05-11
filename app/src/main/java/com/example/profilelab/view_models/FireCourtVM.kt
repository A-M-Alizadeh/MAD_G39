package com.example.profilelab.view_models

data class FireCourtVM (
    val id: String,
    val name: String,
    val location : HashMap<String, String>,
    val sports : HashMap<Int, String>
) {
    override fun toString(): String {
        return name
    }
}