package com.estevaosouza.weatherappproject.repository

import com.estevaosouza.weatherappproject.api.WeatherApiServices
import com.estevaosouza.weatherappproject.util.Constants

class WeatherRepository(private val api: WeatherApiServices) {

    // Call Directly Current Weather API
    suspend fun getCurrentWeather(
        city: String, unit: String
    ) = api.getCurrentWeather(city, unit, Constants.API_KEY)

    // Call Directly Forecast Weather API
    suspend fun getForecastWeather(
        latitude: Double, longitude: Double, unit: String
    ) = api.getForecastWeather(latitude, longitude, unit, Constants.API_KEY)
}