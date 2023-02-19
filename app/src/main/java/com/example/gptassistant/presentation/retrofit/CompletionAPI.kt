package com.myjokes.app.wearable.composestarter.presentation.retrofit


import com.google.gson.JsonObject
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface CompletionAPI {


    @GET("completions/")
    suspend fun getCompletionPrompt(@Body requestBody: JsonObject): Response<ResponseBody>

}