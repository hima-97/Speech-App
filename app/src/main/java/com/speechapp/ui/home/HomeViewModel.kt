package com.speechapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    // Mutable LiveData for welcome message
    private val welcomeMessage = MutableLiveData<String>().apply {
        value = "Welcome to the Speech App!\n\nPick an option from the top-left menu!"

    }
    val welcome: LiveData<String> = welcomeMessage
}