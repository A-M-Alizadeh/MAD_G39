package com.example.profilelab.view_models

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FriendsViewModel: ViewModel(){
    val friendsList : MutableLiveData<List<Friend>> by lazy {
        MutableLiveData<List<Friend>>()
    }
    val db = FirebaseFirestore.getInstance()

    init {
        getPeople()
    }

    fun getPeople() {
        val user = FirebaseAuth.getInstance().currentUser
        val friends = arrayListOf<Friend>()
        Log.e(TAG, "*****> This User: ${user?.uid}")

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.e(TAG, "User: ${document.id} => Current: ${user?.uid} , ${document.id==user?.uid.toString()}")

                        if (document.id==user?.uid.toString()) {
                            continue
                        }
                        else {
                            friends.add(
                                Friend(
                                    document.id,
                                    document.data["username"] as String,
                                    document.data["nickname"] as String,
                                    document.data["interests"] as ArrayList<String>
                                )
                            )
                        }
                    }

                Log.e(TAG, "getFriends: $friends")
                friendsList.postValue(friends)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting Users(Friends) documents.", exception)
            }
    }

}