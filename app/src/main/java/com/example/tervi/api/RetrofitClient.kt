package com.example.tervi.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Para pruebas en dispositivo físico usa tu IP local (ej: 192.168.1.74)
    // Para pruebas en emulador usa 10.0.2.2
    private const val BASE_URL = "http://192.168.1.81/tervi_api/"

    private val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(logger)
        .connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttp)
            .build()

        retrofit.create(ApiService::class.java)
    }
}