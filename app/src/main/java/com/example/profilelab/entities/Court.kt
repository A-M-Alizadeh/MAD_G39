package com.example.profilelab.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// ----------------- Court -----------------
@Entity(tableName = "courts",
    foreignKeys = [ForeignKey(
        entity = Sport::class,
        childColumns = ["sport_id"],
        parentColumns = ["id"]
    )])
data class Court (
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "sport_id") val sport_id: Int,
)