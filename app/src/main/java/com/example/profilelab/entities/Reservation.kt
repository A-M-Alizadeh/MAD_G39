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
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "time") val time: String?,
    @ColumnInfo(name = "status") val status: Boolean?,
    @ColumnInfo(name = "time_slot_id") val time_slot_id: Int,
)