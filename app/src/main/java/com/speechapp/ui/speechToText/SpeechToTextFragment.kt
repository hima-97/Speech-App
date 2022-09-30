package com.speechapp.ui.speechToText

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import com.speechapp.databinding.FragmentSpeechToTextBinding
import java.util.*

@Suppress("DEPRECATION", "RemoveExplicitTypeArguments")
class SpeechToTextFragment : Fragment()
{
    private var _binding: FragmentSpeechToTextBinding? = null

    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    private val requestCodeSpeech = 100

    //private val speechToTextViewModel = ViewModelProvider(this).get(SpeechToTextViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val speechToTextViewModel = ViewModelProvider(this).get(SpeechToTextViewModel::class.java)

        _binding = FragmentSpeechToTextBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val outputTextObserver = Observer<String>
        {
                _ -> speakIn()

            Log.i("SpeechToTextFragment", "outputText updated")
        }
        // Using the observer above to observe whenever the inputMessage mutable live data has changed
        speechToTextViewModel.getOutputText().observe(viewLifecycleOwner, outputTextObserver)
        // Same as: textToSpeechViewModel.inputMessage.observe(viewLifecycleOwner, inputMessageObserver)

        binding.fab.setOnClickListener {
            speakIn()
        }

        return root
    }

    // Function to speak out the user input
    private fun speakIn()
    {
        val myIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

        myIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        myIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)

        // prompt text is shown on screen to tell user what to say
        myIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something")

        try {
            startActivityForResult(myIntent, requestCodeSpeech)
        }
        catch (e: Exception) {
            Toast.makeText(context, "No speech", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        val speechToTextViewModel = ViewModelProvider(this).get(SpeechToTextViewModel::class.java)
        when(requestCode)
        {
            requestCodeSpeech -> {
                if(resultCode == Activity.RESULT_OK && null != intent)
                {
                    val list = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    if (list != null) speechToTextViewModel.setOutputText(list[0])
                }
            }
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }
}