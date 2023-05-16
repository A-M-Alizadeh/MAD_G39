package com.example.profilelab.view_models

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.profilelab.AppDB
import com.example.profilelab.models.FullReservation
import com.example.profilelab.repositories.ReservationRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class ReserveViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ReservationRepository

    val allFullReservations: MutableLiveData<List<FullReservation>> by lazy {
        MutableLiveData<List<FullReservation>>()
    }
    val db = FirebaseFirestore.getInstance()

    init {

        val dao = AppDB.getDatabase(application).reservationDao()
        repository = ReservationRepository(dao)
        getReservations()
    }
    private fun getReservations() {
        var reserveList = arrayListOf<FullReservation>()

        db.collection("reservations")
            .whereEqualTo("status", true)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    val dbReservation = document.data

                    reserveList.add(
                        FullReservation(
                            document.id,
                            dbReservation["user_id"] as? Long,
                            0,
                            0,
                            dbReservation["court"] as String,
                            dbReservation["sport"] as String,
                            dbReservation["status"] as Boolean,
                            "gg",
                            "aa",
                            dbReservation["startTime"] as String,
                            dbReservation["endTime"] as String,
                            dbReservation["description"] as String,
                            dbReservation["date"].toString(),
                        )
                    )
                    Log.d("aa", "${document.id} => ${document.data}")
                }

                allFullReservations.postValue(reserveList)
            }
            .addOnFailureListener { exception ->
                Log.w("Reservation fetch error", "Error getting documents.", exception)
            }
    }

    fun updateStatus(id: String, status: Boolean)  {
        val docRef = db.collection("reservations")
        docRef.document(id).update(
            mapOf("status" to status)
        )
        getReservations()
    }

    fun add(reserve: Any) {
        db.collection("reservations")
            .add(reserve)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    ContentValues.TAG,
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
                getReservations()
            }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error adding document", e) }
    }

}
