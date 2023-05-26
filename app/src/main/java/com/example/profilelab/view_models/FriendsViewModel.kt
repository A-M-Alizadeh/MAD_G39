package com.example.profilelab.view_models

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.profilelab.models.FireUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FriendsViewModel: ViewModel(){
    val requestList : MutableLiveData<List<FriendRequest>> by lazy {
        MutableLiveData<List<FriendRequest>>()
    }

    val friendList : MutableLiveData<List<FriendRequest>> by lazy {
        MutableLiveData<List<FriendRequest>>()
    }

    val currentUser: MutableLiveData<FireUser> by lazy {
        MutableLiveData<FireUser>()
    }

    val db = FirebaseFirestore.getInstance()

    init {
        getFriendRequests()
        getFriends()
    }

    fun getFriendRequests() {
        val requests = arrayListOf<FriendRequest>()
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
                                document.data["receiverId"] == currentUser?.uid.toString()
                            )
                        )
                    }
                }
                Log.w(TAG, "=====> requests: $requests")
                requestList.postValue(requests)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting Users(Friends) documents.", exception)
            }
    }

    fun getFriends() {
        val friends = arrayListOf<FriendRequest>()
        val currentUser = FirebaseAuth.getInstance().currentUser

        db.collection("friendship")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if ((document.data["receiverId"] == currentUser?.uid.toString()
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
                                false
                            )
                        )
                    }
                }
                Log.w(TAG, "=====> friends: $friends")
                friendList.postValue(friends)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting Users(Friends) documents.", exception)
            }
    }

    fun changeFriendshipStatus(reqId: String, status: Int){
        db.collection("friendship").document(reqId).update("status", status)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }

    }

}
