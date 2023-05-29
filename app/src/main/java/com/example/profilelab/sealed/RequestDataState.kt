package com.example.profilelab.sealed

import com.example.profilelab.view_models.FriendRequest

sealed class RequestDataState {
    class Success(val data: MutableList<FriendRequest>) : RequestDataState()
    class Failure(val message: String) : RequestDataState()
    object Loading : RequestDataState()
    object Empty : RequestDataState()
}