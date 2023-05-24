package com.example.profilelab.view_models


data class Interest(
    val id: Int,
    val title: String,
    var selected: Boolean,
) {
    override fun toString(): String {
        return title
    }
}