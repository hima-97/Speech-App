package com.speechapp.ui.textToSpeech

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TextToSpeechViewModel : ViewModel()
{
    private val inputMessage : MutableLiveData<String> = MutableLiveData()

    // Setter for input message
    fun setInputMessage(myInput : String)
    {
        inputMessage.value = myInput
    }

    // Getter for input message
    fun getInputMessage() : MutableLiveData<String>
    {
        return inputMessage
    }
}