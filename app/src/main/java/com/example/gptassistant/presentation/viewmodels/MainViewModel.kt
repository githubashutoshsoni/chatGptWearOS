package com.example.gptassistant.presentation.viewmodels

import android.app.Application
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
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Retrofit
import kotlin.coroutines.coroutineContext


class MainViewModel(application: Application) : AndroidViewModel(application) {


    val exceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { CoroutineContext, throwable ->


        }

    var textFromSpeech: MutableSharedFlow<String> = MutableSharedFlow<String>()


    fun saveToken(string: String) {
        val sharedPrefManager = SharedPrefManager.getInstance(getApplication())
        sharedPrefManager.setPreference(Constants.API_TOKEN, string)
    }


    fun requestToAPI(jsonObject: JsonObject) {

        CoroutineScope(exceptionHandler).launch {

            val requestBody = RetrofitManager.getInstance(getApplication()).getCompletionAPI()
            val response = requestBody.getCompletionPrompt(jsonObject)

            if (response.isSuccessful) {


                val rawResponse = response.body().toString()
                val gson =
                    Gson().fromJson(rawResponse, CompletionResponse::class.java)
                textFromSpeech.emit(gson.choices[0].text)

            }

        }

    }


}