package com.myjokes.app.wearable.composestarter.presentation.retrofit


import com.example.gptassistant.presentation.models.CompletionResponse
import com.google.gson.JsonObject
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CompletionAPI {


    @POST("completions")
    suspend fun getCompletionPrompt(@Body requestBody: JsonObject): Response<CompletionResponse>

}