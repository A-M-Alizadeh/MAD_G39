package com.example.profilelab.view_models.liveVM

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.profilelab.models.FireUser
import com.example.profilelab.sealed.PeopleDataState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PeopleLiveViewModel: ViewModel(){
    val response :  MutableState<PeopleDataState> = mutableStateOf(PeopleDataState.Empty)
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()

    init {
        fetchLiveRequests()
    }

    fun fetchLivePeople(requests : MutableList<String> = mutableListOf()) {
        val tempList = mutableListOf<FireUser>()
        response.value = PeopleDataState.Loading
        FirebaseFirestore.getInstance().collection("users")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    response.value = error.message?.let { PeopleDataState.Failure(it) }!!
                    return@addSnapshotListener
                }
                if (value != null) {
                    for (document in value) {
                        if (requests.contains(document.id) || document.id == currentUserId) {
                            continue
                        }
                        else {
                            tempList.add(
                                FireUser(
                                    document.id,
                                    document.data["username"] as String,
                                    document.data["nickname"] as String,
                                    document.data["interests"] as ArrayList<String> ,
                                    document.data["fcmToken"] as String,
                                )
                            )
                        }

                    }
                    response.value = PeopleDataState.Success(tempList)
                }
            }
    }

    fun fetchLiveRequests(){
        val tempList = mutableListOf<String>()
        response.value = PeopleDataState.Loading
        FirebaseFirestore.getInstance().collection("friendship")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    response.value = error.message?.let { PeopleDataState.Failure(it) }!!
                    return@addSnapshotListener
                }
                if (value != null) {
                    for (document in value) {
                        if ((document.data["receiverId"] == currentUserId
                                    || document.data["senderId"] == currentUserId )
                            && (document.data["status"] == 1.toLong()
                                    || document.data["status"] == 0.toLong())) {
                            tempList.add(
                                if (document.data["receiverId"] == FirebaseAuth.getInstance().currentUser?.uid.toString())
                                    document.data["senderId"] as String
                                else
                                    document.data["receiverId"] as String
                            )
                        }
                    }
                    fetchLivePeople(tempList)
                }
            }
    }

    fun sendLiveFriendRequest(guy: FireUser){
        //this code should add a new document to the friendship collection
//        val user = FirebaseAuth.getInstance().currentUser
        //fixing the error in dirty way
        var usr: FireUser? = null

        FirebaseFirestore.getInstance().collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.id == FirebaseAuth.getInstance().currentUser?.uid.toString()) {
                        usr = FireUser(
                            document.id,
                            document.data["username"] as String,
                            document.data["nickname"] as String,
                            document.data["interests"] as ArrayList<String>,
                            document.data["fcmToken"] as String,
                        )
                    }
                }

                //old part
                Log.d(ContentValues.TAG, "=======> guy sendFriendRequest to: $guy")
                Log.d(ContentValues.TAG, "=======> Auth sendFriendRequest to: ${FirebaseAuth.getInstance().currentUser?.uid.toString()}")
                Log.d(ContentValues.TAG, "=======> me sendFriendRequest to: ${usr}")
                val friendRequestData = hashMapOf(
                    "senderId" to usr?.id,
                    "receiverId" to guy.id,
                    "status" to 0,
                    "senderUsername" to usr?.username,
                    "senderNickname" to usr?.nickname,
                    "receiverUsername" to guy.username,
                    "receiverNickname" to guy.nickname,
                    "senderFcmToken" to usr?.fcmToken,
                    "receiverFcmToken" to guy.fcmToken,
                )
                FirebaseFirestore.getInstance().collection("friendship")
                    .add(friendRequestData)
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener {
                            e -> Log.w(ContentValues.TAG, "Error writing document", e)
                    }
                //old part end

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting Users(Friends) documents.", exception)
            }

    }
}