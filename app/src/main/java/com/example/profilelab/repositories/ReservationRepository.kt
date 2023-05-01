package com.example.profilelab.repositories

import androidx.lifecycle.LiveData
import com.example.profilelab.dao.ReservationDao
import com.example.profilelab.models.FullReservation

class ReservationRepository(private val reservationDao: ReservationDao) {

    val allFullReservation: LiveData<List<FullReservation>> = reservationDao.getInDetails()

    suspend fun updateStatus(id: Int, status: Boolean) {
        reservationDao.update(id, status)
    }
}