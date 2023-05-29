package com.example.profilelab.sealed

import com.example.profilelab.models.FireUser

sealed class PeopleDataState {
    class Success(val data: MutableList<FireUser>) : PeopleDataState()
    class Failure(val message: String) : PeopleDataState()
    object Loading : PeopleDataState()
    object Empty : PeopleDataState()
}