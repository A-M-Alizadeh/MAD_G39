package com.example.profilelab.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.profilelab.AppDB
import com.example.profilelab.models.FullReservation
import com.example.profilelab.repositories.ReservationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReserveViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ReservationRepository

    val allFullReservations: LiveData<List<FullReservation>>

    init {
        val dao = AppDB.getDatabase(application).reservationDao()
        repository = ReservationRepository(dao)
        allFullReservations = repository.allFullReservation
    }

    fun updateStatus(id: Int, status: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateStatus(id, status)
    }

}
