package com.myjokes.app.wearable.composestarter.presentation.retrofit


import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface CompletionAPI {


    @GET("completions/")
    suspend fun getCompletionPrompt(): Response<ResponseBody>

}