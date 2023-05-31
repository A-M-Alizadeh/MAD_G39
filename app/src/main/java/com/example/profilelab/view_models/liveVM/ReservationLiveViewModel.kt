package com.example.profilelab.view_models.liveVM

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.profilelab.sealed.RequestDataState
import com.example.profilelab.sealed.ReservationsDataState
import com.example.profilelab.view_models.ReservationLiveModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class ReservationLiveViewModel: ViewModel() {
    val response : MutableState<ReservationsDataState> = mutableStateOf(ReservationsDataState.Empty)
    var resId = mutableStateOf ("")
    val db = FirebaseFirestore.getInstance()

    fun setReservationID(reservationID: String){
        resId.value = reservationID
        fetchResevation(reservationID = resId.value)
    }

    fun fetchResevation(reservationID: String){
        response.value = ReservationsDataState.Loading
        var tempReservation = mutableStateOf(ReservationLiveModel("","","","","","","",false,"",0))
        db.addSnapshotsInSyncListener() {
            db.collection("reservations")
                .document(reservationID).get()
                .addOnSuccessListener {
                    tempReservation.value = ReservationLiveModel(
                        it.id,
                        it.getString("court").toString(),
                        it.getString("sport").toString(),
                        it.getString("date").toString(),
                        it.getString("startTime").toString(),
                        it.getString("endTime").toString(),
                        it.getString("description").toString(),
                        !it.getBoolean("isRated")!!,
                        it.getString("comment").toString(),
                        it.getLong("rate")!!.toInt(),
                    )
                    response.value = ReservationsDataState.Success(tempReservation)
                }
        }
    }

}