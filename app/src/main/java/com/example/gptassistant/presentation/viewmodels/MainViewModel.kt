package com.example.gptassistant.presentation.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.gptassistant.presentation.helpers.Constants
import com.resmed.mynight.managers.SharedPrefManager


class MainViewModel(application: Application) : AndroidViewModel(application) {


    var textFromSpeech: String? by mutableStateOf(null)


    fun saveToken(string: String) {
        val sharedPrefManager = SharedPrefManager.getInstance(getApplication())
        sharedPrefManager.setPreference(Constants.API_TOKEN, string)
    }




}