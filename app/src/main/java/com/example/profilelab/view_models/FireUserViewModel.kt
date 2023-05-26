package com.example.profilelab.view_models

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.profilelab.Login
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

    fun sendFriendRequest(guy: FireUser){
        val user = FirebaseAuth.getInstance().currentUser
        val friendRequestData = hashMapOf(
            "senderId" to user?.uid.toString(),
            "receiverId" to guy.id,
            "status" to 0,
            "senderUsername" to guy.username,
            "senderNickname" to guy.nickname,
        )
        db.collection("friendship")
            .add(friendRequestData)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener {
                    e -> Log.w(TAG, "Error writing document", e)
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
