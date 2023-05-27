package com.example.profilelab.view_models

import com.example.profilelab.models.FireUser


data class FriendRequest(
    val id: String,
    val senderId: String,
    val receiverId: String,
    var status: Long, // 0: pending, 1: accepted, 2: rejected
    val senderUsername: String,
    val senderNickname: String,
    val receiverUsername: String,
    val receiverNickname: String,
    val incoming: Boolean = false,
    val senderFcmToken: String = "",
    val receiverFcmToken: String = "",
) {
    override fun toString(): String {
        return "FriendRequest(id='$id', senderId='$senderId', receiverId='$receiverId', status=$status, senderUsername='$senderUsername', senderNickname='$senderNickname', incoming=$incoming)"
    }
}