package com.example.profilelab.view_models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

val tempList = listOf(
    "soccer",
    "basketball",
    "tennis",
    "baseball",
    "golf",
    "running",
    "volleyball",
    "badminton",
    "swimming",
    "boxing",
    "table tennis",
    "skiing",
    "ice skating",
    "roller skating",
    "cricket",
    "rugby",
    "pool",
    "darts",
    "football",
    "bowling",
    "ice hockey",
    "surfing",
    "karate",
    "horse racing",
    "snowboarding",
    "skateboarding",
    "cycling",
    "archery",
    "fishing",
    "gymnastics",
    "figure skating",
    "rock climbing",
    "sumo wrestling",
    "taekwondo",
    "fencing",
    "water skiing",
    "jet skiing",
    "weight lifting",
    "scuba diving",
    "judo",
    "wind surfing",
    "kickboxing",
    "sky diving",
    "hang gliding",
    "bungee jumping",
)

class InterestViewModel: ViewModel(){
        val interests : MutableLiveData<List<Interest>> by lazy { MutableLiveData<List<Interest>>() }

        init {
            getInterests()
        }

        private fun getInterests(){
            val list = mutableListOf<Interest>()
            for (i in tempList.indices) {
                list.add(Interest(i, tempList[i], false))
            }
            interests.postValue(list)
        }

        fun changeState(position: Int){
            val interest = interests.value?.get(position)
            if (interest != null) {
                interest.selected = !interest.selected
            }
            interests.postValue(interests.value)
            if (interest != null) {
                Log.e("InterestViewModel", "changeState: ${interest.title} $position")
            }
        }

}