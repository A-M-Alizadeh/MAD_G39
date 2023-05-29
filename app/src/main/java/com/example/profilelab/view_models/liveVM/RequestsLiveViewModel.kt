package com.example.profilelab.view_models.liveVM

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import com.example.profilelab.sealed.RequestDataState
import com.example.profilelab.view_models.FriendRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RequestsLiveViewModel: ViewModel() {
    val response : MutableState<RequestDataState> = mutableStateOf(RequestDataState.Empty)
    var tempRequest:FriendRequest? = null
    val tempList = mutableListOf<FriendRequest>()

    init {
        fetchLiveRequests()
    }

    fun fetchLiveRequests(){
        tempList.clear()
        val currentUserId = FirebaseAuth.getInstance().uid.toString()
        response.value = RequestDataState.Loading
        FirebaseFirestore
            .getInstance()
            .collection("friendship")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    response.value = error.message?.let { RequestDataState.Failure(it) }!!
                    return@addSnapshotListener
                }
                if (value != null) {
                    tempList.clear()
                    for (document in value) {
                        //check if snapshot contains current user id

                        if ((document.data["senderId"] == currentUserId
                                    || document.data["receiverId"] == currentUserId)
                            && document.data["status"] == 0.toLong()) {
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
                    response.value = RequestDataState.Success(tempList)

                }
            }
    }

    fun changeFriendshipStatus(reqId: String, status: Int){ // 0 - pending, 1 - accepted, 2 - declined 3 - canceled
        FirebaseFirestore
            .getInstance()
            .collection("friendship")
            .document(reqId)
            .update("status", status)
            .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully updated!")

            }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error updating document", e) }
    }

}