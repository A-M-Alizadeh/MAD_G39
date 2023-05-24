package com.example.profilelab.view_models

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class FriendsViewModel: ViewModel(){
    val friendsList : MutableLiveData<List<Friend>> by lazy {
        MutableLiveData<List<Friend>>()
    }
    val db = FirebaseFirestore.getInstance()

    init {
        getFriends()
    }

    fun getFriends() {
        val friends = arrayListOf<Friend>()

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    friends.add(
                        Friend(
                            document.id,
                            document.data["username"] as String,
                            document.data["nickname"] as String,
                        )
                    )
                }
                Log.e(TAG, "getFriends: $friends")
                friendsList.postValue(friends)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting Users(Friends) documents.", exception)
            }
    }


}