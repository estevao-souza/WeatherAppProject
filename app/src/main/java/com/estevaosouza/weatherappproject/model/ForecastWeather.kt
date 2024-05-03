package com.estevaosouza.weatherappproject.model

import com.google.gson.annotations.SerializedName

data class ForecastWeather(
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val minutely: List<Minutely>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Long
)