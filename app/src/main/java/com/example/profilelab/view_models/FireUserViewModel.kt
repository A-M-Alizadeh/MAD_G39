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
                        && (document.data["status"] == 1.toLong()
                                || document.data["status"] == 0.toLong())) {
//                        Log.d("requests", "111111> "+document.data)
                        if (document.data["receiverId"] == user?.uid.toString())
                            allRequests.add(document.data["senderId"] as String)
                        else
                            allRequests.add(document.data["receiverId"] as String)
                    }
                }

                //new
                db.collection("users")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Log.e(TAG, "what does it have ? $allRequests")

                            if (allRequests.contains(document.id)) {
                                continue
                            }
                            else {
                                users.add(
                                    FireUser(
                                        document.id,
                                        document.data["username"] as String,
                                        document.data["nickname"] as String,
                                        document.data["interests"] as ArrayList<String>,
                                        document.data["fcmToken"] as String,
                                    )
                                )
                            }
                        }
                        usersList.postValue(users)
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting Users(Friends) documents.", exception)
                    }
                //new

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting Users(Friends) documents.", exception)
            }
        //lets see


    }

    fun sendFriendRequest(guy: FireUser){
//        val user = FirebaseAuth.getInstance().currentUser
        //fixing the error in dirty way
        var usr: FireUser? = null

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
                            document.data["fcmToken"] as String,
                        )
                    }
                }
                currentUser.postValue(usr)

                //old part
                Log.d(TAG, "=======> guy sendFriendRequest to: $guy")
                Log.d(TAG, "=======> Auth sendFriendRequest to: ${FirebaseAuth.getInstance().currentUser?.uid.toString()}")
                Log.d(TAG, "=======> me sendFriendRequest to: ${usr}")
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
                //old part end

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting Users(Friends) documents.", exception)
            }
        //fixing the error in dirty way
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
                            document.data["fcmToken"] as String,
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
