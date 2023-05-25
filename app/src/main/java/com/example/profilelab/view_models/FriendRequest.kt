package com.example.profilelab.view_models


data class FriendRequest(
    val id: String,
    val incoming: Boolean,
    val person: Friend,
) {
    override fun toString(): String {
        return person.toString()
    }
}