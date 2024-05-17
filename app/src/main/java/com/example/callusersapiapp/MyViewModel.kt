package com.example.callusersapiapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel(private val connectivityRepository: ConnectivityRepository) : ViewModel()  {
    val isOnline = connectivityRepository.isConnected as LiveData<*>

}