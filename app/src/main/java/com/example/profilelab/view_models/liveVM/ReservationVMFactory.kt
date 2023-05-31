package com.example.profilelab.view_models.liveVM

import androidx.lifecycle.ViewModelProvider

inline fun <VM : ReservationLiveViewModel> viewModelFactory(crossinline f: () -> VM) =
    object : ViewModelProvider.Factory {
        fun <T : ReservationLiveViewModel> create(aClass: Class<T>):T = f() as T
    }