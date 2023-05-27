package com.example.profilelab.view_models

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.profilelab.models.FireUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FriendsViewModel: ViewModel(){
    val requestList : MutableLiveData<SnapshotStateList<FriendRequest>> by lazy {
        MutableLiveData<SnapshotStateList<FriendRequest>>()
    }

    val friendList : MutableLiveData<SnapshotStateList<FriendRequest>> by lazy {
        MutableLiveData<SnapshotStateList<FriendRequest>>()
    }

    val currentUser: MutableLiveData<FireUser> by lazy {
        MutableLiveData<FireUser>()
    }

    val db = FirebaseFirestore.getInstance()

    init {
        getFriendRequests()
//        getFriends()
    }

    fun getFriendRequests() {
        val requests = SnapshotStateList<FriendRequest>()
        val friends = SnapshotStateList<FriendRequest>()
        val currentUser = FirebaseAuth.getInstance().currentUser
        requestList.postValue(requests)

        db.collection("friendship")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if ((document.data["senderId"] == currentUser?.uid.toString()
                                || document.data["receiverId"] == currentUser?.uid.toString())
                        && document.data["status"] == 0.toLong()) {
                        requests.add(
                            FriendRequest(
                                document.id as String,
                                document.data["senderId"] as String,
                                document.data["receiverId"] as String,
                                document.data["status"] as Long,
                                document.data["senderUsername"] as String,
                                document.data["senderNickname"] as String,
                                document.data["receiverUsername"] as String,
                                document.data["receiverNickname"] as String,
                                document.data["receiverId"] == currentUser?.uid.toString(),
                                document.data["senderFcmToken"] as String,
                                document.data["receiverFcmToken"] as String
                            )
                        )
                    }
                    else if ((document.data["receiverId"] == currentUser?.uid.toString()
                                || document.data["senderId"] == currentUser?.uid.toString() )
                        && document.data["status"] == 1.toLong()) {
                        friends.add(
                            FriendRequest(
                                document.id as String,
                                document.data["senderId"] as String,
                                document.data["receiverId"] as String,
                                document.data["status"] as Long,
                                document.data["senderUsername"] as String,
                                document.data["senderNickname"] as String,
                                document.data["receiverUsername"] as String,
                                document.data["receiverNickname"] as String,
                                false,
                                document.data["senderFcmToken"] as String,
                                document.data["receiverFcmToken"] as String
                            )
                        )
                    }
                }
                Log.w(TAG, "=====> requests: $requests")
                requestList.postValue(requests)
                friendList.postValue(friends)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting Users(Friends) documents.", exception)
            }
    }

    fun changeFriendshipStatus(reqId: String, status: Int){ // 0 - pending, 1 - accepted, 2 - declined 3 - canceled
        db.collection("friendship").document(reqId).update("status", status)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!")

//                requestList.value?.removeIf { it.id == reqId }
//                requestList.postValue(requestList.value)

                val requests = SnapshotStateList<FriendRequest>()
                val friends = SnapshotStateList<FriendRequest>()
                val currentUser = FirebaseAuth.getInstance().currentUser

                db.collection("friendship")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            if ((document.data["senderId"] == currentUser?.uid.toString()
                                        || document.data["receiverId"] == currentUser?.uid.toString())
                                && document.data["status"] == 0.toLong()) {
                                requests.add(
                                    FriendRequest(
                                        document.id as String,
                                        document.data["senderId"] as String,
                                        document.data["receiverId"] as String,
                                        document.data["status"] as Long,
                                        document.data["senderUsername"] as String,
                                        document.data["senderNickname"] as String,
                                        document.data["receiverUsername"] as String,
                                        document.data["receiverNickname"] as String,
                                        document.data["receiverId"] == currentUser?.uid.toString(),
                                        document.data["senderFcmToken"] as String,
                                        document.data["receiverFcmToken"] as String
                                    )
                                )
                            }
                            else if ((document.data["receiverId"] == currentUser?.uid.toString()
                                        || document.data["senderId"] == currentUser?.uid.toString() )
                                && document.data["status"] == 1.toLong()) {
                                friends.add(
                                    FriendRequest(
                                        document.id as String,
                                        document.data["senderId"] as String,
                                        document.data["receiverId"] as String,
                                        document.data["status"] as Long,
                                        document.data["senderUsername"] as String,
                                        document.data["senderNickname"] as String,
                                        document.data["receiverUsername"] as String,
                                        document.data["receiverNickname"] as String,
                                        false,
                                        document.data["senderFcmToken"] as String,
                                        document.data["receiverFcmToken"] as String
                                    )
                                )
                            }
                        }
                        Log.w(TAG, "=====> requests: $requests")
                        requestList.postValue(requests)
                        friendList.postValue(friends)
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting Users(Friends) documents.", exception)
                    }
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }

}
