package com.estevaosouza.weatherappproject.api

import com.estevaosouza.weatherappproject.model.CurrentWeather
import com.estevaosouza.weatherappproject.model.ForecastWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiServices {

    @GET("2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("units") unit: String,
        @Query("appid") apiKey: String
    ) : Response<CurrentWeather>

    @GET("3.0/onecall")
    suspend fun getForecastWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") unit: String,
        @Query("appid") apiKey: String
    ) : Response<ForecastWeather>
}