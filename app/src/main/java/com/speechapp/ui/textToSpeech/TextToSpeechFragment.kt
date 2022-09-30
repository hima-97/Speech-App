package com.speechapp.ui.textToSpeech

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.speechapp.databinding.FragmentTextToSpeechBinding
import java.util.*

class TextToSpeechFragment : Fragment(), TextToSpeech.OnInitListener
{
    // Variable of FragmentTextToSpeechBinding type used for binding
    private var _binding: FragmentTextToSpeechBinding? = null

    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    // Variable of TextToSpeech type
    private var tts: TextToSpeech? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Instance of TextToSpeechViewModel view model
        val textToSpeechViewModel = ViewModelProvider(this).get(TextToSpeechViewModel::class.java)

        _binding = FragmentTextToSpeechBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Using constructor to initialize TextToSpeech type variable
        tts = TextToSpeech(context, this)

        // Observer for input message
        // Observing input message from XML file
        val inputMessageObserver = Observer<String>
        {
            // Everytime observer is triggered, it means the inputMessage mutable live data has changed
            // We use the following lambda function to call the speakOut() function with the updated input
            input -> speakOut(input)

            Log.i("TextToSpeechFragment", "inputMessage updated")
        }
        // Using the observer above to observe whenever the inputMessage mutable live data has changed
        textToSpeechViewModel.getInputMessage().observe(viewLifecycleOwner, inputMessageObserver)
        // Same as: textToSpeechViewModel.inputMessage.observe(viewLifecycleOwner, inputMessageObserver)

        // Code for when FAB on text to speech page is clicked
        binding.fab.setOnClickListener {
            if(binding.inputMessageXML.text.toString().isEmpty())
            {
                Snackbar.make(it, "No input entered", Snackbar.LENGTH_LONG).show()
            }
            else
            {
                // Call the setter for inputMessage mutable live data
                // Whenever the setter is called (i.e. mutable live data is changed), the observer is triggered
                textToSpeechViewModel.setInputMessage(binding.inputMessageXML.text.toString())
                // Same as: textToSpeechViewModel.inputMessage = binding.inputMessageXML.text.toString()
            }
        }
        return root
    }

    // Function to speak out the user input
    private fun speakOut(input:String)
    {
        tts!!.speak(input, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    // Function onInit to signal the completion of the TextToSpeech engine initialization
    override fun onInit(status: Int)
    {
        if (status == TextToSpeech.SUCCESS)
        {
            // Set US English as language for text to speech
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Log.e("TTS","The Language specified is not supported!")
            }
        } else
        {
            Log.e("TTS", "Initialization Failed!")
        }
    }

    override fun onDestroyView()
    {
        // Shutdown TTS
        if (tts != null)
        {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroyView()
        _binding = null
    }
}