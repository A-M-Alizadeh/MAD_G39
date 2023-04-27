package com.example.profilelab.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// ----------------- Court -----------------
@Entity(tableName = "courts")
data class Court (
    @PrimaryKey(autoGenerate = true)
    val id: Int ?= null,
    @ColumnInfo(name = "name") val name: String,
)