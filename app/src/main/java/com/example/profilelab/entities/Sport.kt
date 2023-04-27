package com.example.profilelab.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


// ----------------- Court -----------------
@Entity(tableName = "sports")
data class Sport (
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "title") val title: String,
)
