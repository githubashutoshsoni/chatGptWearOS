package com.example.gptassistant.presentation.retrofit

import android.content.Context
import android.content.ContextWrapper
import com.example.gptassistant.BuildConfig
import com.example.gptassistant.presentation.GPTApp
import com.google.gson.GsonBuilder
import com.myjokes.app.wearable.composestarter.presentation.retrofit.CompletionAPI
import com.myjokes.app.wearable.composestarter.presentation.retrofit.ConnectivityInterceptor
//import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitManager(context: Context) : ContextWrapper(context) {

    companion object {
        private var INSTANCE: RetrofitManager? = null
        private lateinit var retrofit: Retrofit

        fun getInstance(context: Context): RetrofitManager {
            if (INSTANCE == null) {
                initManager(context)
            }
            return INSTANCE!!;
        }

        private fun initManager(context: Context) {
            INSTANCE = RetrofitManager(context)
        }
    }

    private fun getDefaultRetrofit(): Retrofit {
        val token = GPTApp.getToken(baseContext)
        val interceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer $token")
                .build()

            chain.proceed(request)
        }

        val httpClient: OkHttpClient.Builder =
            OkHttpClient.Builder().addInterceptor(interceptor)
                .addInterceptor(ConnectivityInterceptor(baseContext))

        if (BuildConfig.DEBUG) {
            val logInterceptor = HttpLoggingInterceptor()
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logInterceptor)
        }

        val gson = GsonBuilder()
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")// + context.getString(R.string.url_version))
            .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(httpClient.build())
            .build()
        return retrofit
    }


    fun getCompletionAPI(): CompletionAPI = getDefaultRetrofit().create(CompletionAPI::class.java)


}