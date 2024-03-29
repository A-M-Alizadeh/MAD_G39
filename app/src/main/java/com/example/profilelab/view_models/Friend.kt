package com.example.profilelab.view_models

data class Friend(
    val id: String,
    val username: String,
    val nickname: String,
    val interests: ArrayList<String>,
) {
    override fun toString(): String {
        return "$username:  $nickname"
    }
}