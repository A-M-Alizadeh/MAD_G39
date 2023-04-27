package com.example.profilelab.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// ----------------- Reservation -----------------
@Entity(tableName = "reservations",
    foreignKeys = [ForeignKey(
        entity = TimeSlot::class,
        childColumns = ["time_slot_id"],
        parentColumns = ["id"]
    )])
data class Reservation (
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "user_id") val user_id: Int,
    @ColumnInfo(name = "date_") val date_: String,
    @ColumnInfo(name = "court_sports_id") val court_sports_id: Int,
    @ColumnInfo(name = "time_slot_id") val time_slot_id: Int,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "status") val status: Int,
)