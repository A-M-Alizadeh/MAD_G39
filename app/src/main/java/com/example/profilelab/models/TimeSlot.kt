package com.example.profilelab.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// ----------------- Time Slot -----------------
@Entity(tableName = "time_slots")
data class TimeSlot (
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "start_time") val start_time: String,
    @ColumnInfo(name = "end_time") val end_time: String,
)
