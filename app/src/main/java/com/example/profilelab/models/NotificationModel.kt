package com.example.profilelab.models

data class NotificationModel (
    val to: String,
    val data: Data
)
data class Data (
    val title: String,
    val message: String
)