package com.example.profilelab.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// ----------------- Court -----------------
@Entity(tableName = "courts")
data class Court (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "name") val name: String,
)