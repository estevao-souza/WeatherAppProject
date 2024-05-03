package com.estevaosouza.weatherappproject.viewModel

import androidx.lifecycle.ViewModel
import com.estevaosouza.weatherappproject.api.RetrofitHelper
import com.estevaosouza.weatherappproject.api.WeatherApiServices
import com.estevaosouza.weatherappproject.repository.WeatherRepository

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    constructor():this(WeatherRepository(RetrofitHelper().getRetrofit().create(WeatherApiServices::class.java)))

    suspend fun loadCurrentWeather(
        city: String, unit: String
    ) = repository.getCurrentWeather(city, unit)

    suspend fun loadForecastWeather(
        latitude: Double, longitude: Double, unit: String
    ) = repository.getForecastWeather(latitude, longitude, unit)
}