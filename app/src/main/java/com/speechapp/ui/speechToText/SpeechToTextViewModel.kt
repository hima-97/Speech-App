package com.speechapp.ui.speechToText

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SpeechToTextViewModel : ViewModel()
{
    private val outputText : MutableLiveData<String> = MutableLiveData()

    // Setter for output text
    fun setOutputText(myOutput : String)
    {
        outputText.value = myOutput
    }

    // Getter for output text
    fun getOutputText() : MutableLiveData<String>
    {
        return outputText
    }
}