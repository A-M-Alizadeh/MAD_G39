package com.example.profilelab.repositories

import androidx.lifecycle.LiveData
import com.example.profilelab.dao.CourtDao
import com.example.profilelab.models.Court

class CourtRepository(private val courtDao: CourtDao) {
    val all: List<Court> = courtDao.getAll()
}