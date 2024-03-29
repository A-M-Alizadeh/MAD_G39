package com.example.profilelab.models

data class FireUser(
    val id: String,
    val username: String,
    val nickname: String,
    val interests: ArrayList<String>,
    val fcmToken: String,
) {
    override fun toString(): String {
        return "$username $nickname $interests $fcmToken "
    }
}