package com.example.profilelab.view_models.liveVM

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.profilelab.sealed.FriendsDataState
import com.example.profilelab.view_models.FriendRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FriendsLiveViewModel: ViewModel() {
    val response : MutableState<FriendsDataState> = mutableStateOf(FriendsDataState.Empty)
    var tempRequest:FriendRequest? = null

    init {
        fetchLiveFriends()
    }

    fun fetchLiveFriends(){
        val tempList = mutableListOf<FriendRequest>()
        val currentUserId = FirebaseAuth.getInstance().uid.toString()
        response.value = FriendsDataState.Loading
        FirebaseFirestore.getInstance().collection("friendship")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    response.value = error.message?.let { FriendsDataState.Failure(it) }!!
                    return@addSnapshotListener
                }
                if (value != null) {
                    tempList.clear()
                    for (document in value) {
                        if ((document.data["receiverId"] == currentUserId
                                    || document.data["senderId"] == currentUserId )
                            && document.data["status"] == 1.toLong()) {
                            tempRequest = FriendRequest(
                                document.id as String,
                                document.data["senderId"] as String,
                                document.data["receiverId"] as String,
                                document.data["status"] as Long,
                                document.data["senderUsername"] as String,
                                document.data["senderNickname"] as String,
                                document.data["receiverUsername"] as String,
                                document.data["receiverNickname"] as String,
                                document.data["receiverId"] == currentUserId,
                                document.data["senderFcmToken"] as String,
                                document.data["receiverFcmToken"] as String
                            )
                            tempList.add(tempRequest!!)
//                            if (!tempList.contains(tempRequest)) {
//                                tempList.add(tempRequest!!)
//                            }
                        }
                    }
//                    tempList = tempList.distinct().toMutableList()
                    response.value = FriendsDataState.Success(tempList)

                }
            }
    }
}