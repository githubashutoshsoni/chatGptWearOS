package com.example.gptassistant.presentation

import android.app.Application
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue



class MainViewModel(application: Application) : AndroidViewModel(application) {



    var textFromSpeech: String? by mutableStateOf(null)




}