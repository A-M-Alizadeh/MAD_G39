package com.example.profilelab.view_models

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.profilelab.models.FireUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log

class FireUserViewModel: ViewModel(){
    val usersList : MutableLiveData<SnapshotStateList<FireUser>> by lazy {
        MutableLiveData<SnapshotStateList<FireUser>>()
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
        val users = SnapshotStateList<FireUser>()
//        val allRequests = findRequests()


        //lets see
        var allRequests = arrayListOf<String>()
        db.collection("friendship")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if ((document.data["receiverId"] == user?.uid.toString()
                                || document.data["senderId"] == user?.uid.toString() )
                        && (document.data["status"] == 2.toLong()
                                || document.data["status"] == 0.toLong())) {
//                        Log.d("requests", "111111> "+document.data)
                        if (document.data["receiverId"] == user?.uid.toString())
                            allRequests.add(document.data["senderId"] as String)
                        else
                            allRequests.add(document.data["receiverId"] as String)
                    }
                }
//                Log.d("requests", "000000> "+allRequests.distinct().toList().toString())
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting Users(Friends) documents.", exception)
            }
        //lets see

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.e(TAG, "what does it have ? $allRequests")

                    if (allRequests.contains(document.id) || document.id == user?.uid.toString()) {
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

                usersList.value?.removeIf { it.id == guy.id }
                usersList.postValue(usersList.value)

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

    fun findRequests():List<String>{
        var currentUser = FirebaseAuth.getInstance().currentUser
        var requests = arrayListOf<String>()

        db.collection("friendship")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if ((document.data["receiverId"] == currentUser?.uid.toString()
                                || document.data["senderId"] == currentUser?.uid.toString() )
                        && (document.data["status"] == 2.toLong())) {
                        Log.d("requests", "=======Came Here =========")
                        Log.d("requests", document.data.toString())
                        if (document.data["receiverId"] == currentUser?.uid.toString())
                            requests.add(document.data["senderId"] as String)
                        else
                            requests.add(document.data["receiverId"] as String)
                    }
                }
                Log.d("requests", requests.distinct().toList().toString())
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting Users(Friends) documents.", exception)
            }
        return requests
    }

}
