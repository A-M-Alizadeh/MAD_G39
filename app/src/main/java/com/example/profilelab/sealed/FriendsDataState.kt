package com.example.profilelab.sealed

import com.example.profilelab.view_models.FriendRequest

sealed class FriendsDataState {
    class Success(val data: MutableList<FriendRequest>) : FriendsDataState()
    class Failure(val message: String) : FriendsDataState()
    object Loading : FriendsDataState()
    object Empty : FriendsDataState()
}