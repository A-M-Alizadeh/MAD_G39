package com.example.profilelab.sealed
import androidx.compose.runtime.MutableState
import com.example.profilelab.view_models.ReservationLiveModel

sealed class ReservationsDataState {
    class Success(val data: MutableState<ReservationLiveModel>) : ReservationsDataState()
    class Failure(val message: String) : ReservationsDataState()
    object Loading : ReservationsDataState()
    object Empty : ReservationsDataState()
}