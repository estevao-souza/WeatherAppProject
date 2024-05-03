package com.estevaosouza.weatherappproject.model

import com.google.gson.annotations.SerializedName

data class Daily(
    @SerializedName("dew_point")
    val dewPoint: Double,
    val dt: Long,
    @SerializedName("feels_like")
    val feelsLike: FeelsLike,
    val humidity: Int,
    val pressure: Int,
    val summary: String,
    val sunrise: Long,
    val sunset: Long,
    val temp: Temp,
    val uvi: Double,
    val weather: List<Weather>,
    @SerializedName("wind_deg")
    val windDeg: Int,
    @SerializedName("wind_gust")
    val windGust: Double,
    @SerializedName("wind_speed")
    val windSpeed: Double
)