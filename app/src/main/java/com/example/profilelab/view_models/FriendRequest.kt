package com.example.profilelab.view_models

data class FriendRequest(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val username: String,
    val nickname: String,
    val incoming: Boolean,
) {
    override fun toString(): String {
        return "$username:  $nickname"
    }
}