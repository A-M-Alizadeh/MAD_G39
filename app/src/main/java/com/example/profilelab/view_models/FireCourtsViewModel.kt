package com.example.profilelab.view_models

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class FireCourtsViewModel: ViewModel(){
    val courtList : MutableLiveData<List<FireCourt>> by lazy {
        MutableLiveData<List<FireCourt>>()
    }
    val db = FirebaseFirestore.getInstance()

    init {
        getCourts()
    }

    fun getCourts() {
        val courts = arrayListOf<FireCourt>()
        Log.e(ContentValues.TAG, " ------ Getting Courts ${courtList.value}")

        db.collection("courts")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    courts.add(
                        FireCourt(
                            document.id,
                            document.data["name"] as String,
                            document.data["location"] as HashMap<String, Double>,
                            document.data["sports"] as HashMap<String, String>,
                            document.data["address"] as String
                        )
                    )
                }
                courtList.postValue(courts)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting Users(Friends) documents.", exception)
            }
    }
}