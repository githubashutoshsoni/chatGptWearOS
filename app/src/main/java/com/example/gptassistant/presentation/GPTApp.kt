package com.example.gptassistant.presentation

import android.app.Application
import android.content.Context
import com.example.gptassistant.presentation.helpers.Constants
import com.resmed.mynight.managers.SharedPrefManager

class GPTApp : Application() {


    companion object {

        fun getToken(ctx: Context): String {
            val token = SharedPrefManager.getInstance(ctx).getPreference(Constants.API_TOKEN)
            return token ?: ""
        }
    }

}