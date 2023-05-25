package com.example.profilelab.models

import com.example.profilelab.view_models.Friend
import com.example.profilelab.view_models.FriendRequest

data class FireUser(
    val id: String,
    val username: String,
    val nickname: String,
    val interests: ArrayList<String>,
    val friends: ArrayList<Friend>,
    val friendRequests: ArrayList<FriendRequest>
) {
    override fun toString(): String {
        return username
    }
}