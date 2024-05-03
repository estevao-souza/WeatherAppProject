package com.estevaosouza.weatherappproject.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {

    private lateinit var retrofit: Retrofit

    fun getRetrofit(): Retrofit {
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/")
            .addConverterFactory(GsonConverterFactory.create()) // JSON or XML
            .build()

        return retrofit
    }
}