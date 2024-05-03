package com.estevaosouza.weatherappproject.model

data class CurrentWeather(
    val cod: Int,
    val coord: Coord,
    val dt: Long,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Long,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)