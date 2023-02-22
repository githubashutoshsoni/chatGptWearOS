package com.example.gptassistant.presentation.viewmodels

import android.app.Application
import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.gptassistant.presentation.helpers.Constants
import com.example.gptassistant.presentation.models.CompletionResponse
import com.example.gptassistant.presentation.retrofit.RetrofitManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.resmed.mynight.managers.SharedPrefManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Retrofit
import java.util.*
import kotlin.coroutines.coroutineContext


class MainViewModel(application: Application) : AndroidViewModel(application) {


    val exceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { CoroutineContext, throwable ->
            throwable.printStackTrace()
        }


    init {
        SharedPrefManager.getInstance(getApplication()).setPreference(
            Constants.API_TOKEN,
            "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"
        )
    }

    var textFromSpeech: MutableSharedFlow<String> = MutableSharedFlow<String>()

    private var textToSpeech: TextToSpeech? = null


    private var onCHangeText = combine(textFromSpeech){



    }


    fun textToSpeech(text:String) {

        val textToSpeech = TextToSpeech(
            getApplication()
        ) {
            if (it == TextToSpeech.SUCCESS) {
                textToSpeech?.let { txtToSpeech ->
                    txtToSpeech.language = Locale.US
                    txtToSpeech.setSpeechRate(1.0f)
                    txtToSpeech.speak(
                        text,
                        TextToSpeech.QUEUE_ADD,
                        null,
                        null
                    )
                }
            }
        }

    }

    fun saveToken(string: String) {
        val sharedPrefManager = SharedPrefManager.getInstance(getApplication())
        sharedPrefManager.setPreference(Constants.API_TOKEN, string)
    }


    fun requestToAPI(jsonObject: JsonObject) {

        CoroutineScope(exceptionHandler).launch {

            val requestBody = RetrofitManager.getInstance(getApplication()).getCompletionAPI()
            val response = requestBody.getCompletionPrompt(jsonObject)

            if (response.isSuccessful) {


                val rawResponse = response.body()

                textFromSpeech.emit(rawResponse!!.choices[0].text)

            }

        }

    }


}