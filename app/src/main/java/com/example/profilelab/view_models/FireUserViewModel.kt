package com.example.profilelab.view_models

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.profilelab.models.FireUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class FireUserViewModel: ViewModel(){
    val usersList : MutableLiveData<List<FireUser>> by lazy {
        MutableLiveData<List<FireUser>>()
    }
    val currentUser: MutableLiveData<FireUser> by lazy {
        MutableLiveData<FireUser>()
    }

    val db = FirebaseFirestore.getInstance()

    init {
        getPeople()
        findUserById(FirebaseAuth.getInstance().currentUser?.uid.toString())
    }

    fun getPeople() {
        val user = FirebaseAuth.getInstance().currentUser
        val users = arrayListOf<FireUser>()

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.id==user?.uid.toString()) {
                        continue
                    }
                    else {
                        users.add(
                            FireUser(
                                document.id,
                                document.data["username"] as String,
                                document.data["nickname"] as String,
                                document.data["interests"] as ArrayList<String>,
                                document.data["friends"] as ArrayList<Friend>,
                                document.data["friendRequests"] as ArrayList<FriendRequest>
                            )
                        )
                    }
                }
                usersList.postValue(users)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting Users(Friends) documents.", exception)
            }
    }

    fun sendFriendRequest(friendRequest: FriendRequest){
        var usr : FireUser? = null
        var reqs = ArrayList<FriendRequest>()
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.id == FirebaseAuth.getInstance().currentUser?.uid.toString()) {
                        usr = FireUser(
                            document.id,
                            document.data["username"] as String,
                            document.data["nickname"] as String,
                            document.data["interests"] as ArrayList<String>,
                            document.data["friends"] as ArrayList<Friend>,
                            document.data["friendRequests"] as ArrayList<FriendRequest>,
//                                    FriendRequest(
//                                friendRequest.id,
//                                friendRequest.incoming,
//                                friendRequest.person
//                            ) as ArrayList<FriendRequest>
                            //document.data["friendRequests"] as ArrayList<FriendRequest> //List<FriendRequest>
                        )

                        //updating reqs
                        reqs.addAll(currentUser.value?.friendRequests as ArrayList<FriendRequest>)

                        db.collection("users")
                        .document(currentUser.value?.id.toString())
                        .update("friendRequests", reqs as ArrayList<FriendRequest>)
                        .addOnSuccessListener {
                            Log.d(TAG, "++++++ Friend Request Sent")
                        }
                        .addOnFailureListener() {
                            Log.d(TAG, "________ Friend Request Failed")
                        }
                        //updating reqs
                    }
                }

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting Users(Friends) documents.", exception)
            }
    }

    fun findUserById(id:String) {
        var usr: FireUser? = null

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.id == id) {
                        usr = FireUser(
                            document.id,
                            document.data["username"] as String,
                            document.data["nickname"] as String,
                            document.data["interests"] as ArrayList<String>,
                            document.data["friends"] as ArrayList<Friend>,
                            document.data["friendRequests"] as ArrayList<FriendRequest>
                        )
                    }
                }
                currentUser.postValue(usr)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting Users(Friends) documents.", exception)
            }
    }

}
