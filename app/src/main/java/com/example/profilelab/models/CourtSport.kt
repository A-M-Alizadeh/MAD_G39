package com.example.profilelab.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "court_sports",
    foreignKeys = [
        ForeignKey(entity = Court::class, parentColumns = ["id"], childColumns = ["court_id"]),
        ForeignKey(entity = Sport::class, parentColumns = ["id"], childColumns = ["sport_id"])
    ]
)
data class CourtSport(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val court_id: Int,
    val sport_id: Int
)

