package com.estevaosouza.weatherappproject.util

object Constants {
    // Put Here Your OpenWeatherMap API Key
    const val API_KEY = ""

    const val CITY_NAME = "lisbon"

    const val WEATHER_UPDATED_MESSAGE = "Forecast weather data updated"
    const val NOT_FOUND_MESSAGE_ERROR = "City not found"
    const val BAD_REQUEST_MESSAGE_ERROR = "Nothing to geocode"

    const val CURRENT_WEATHER_ERROR_LOG = "Error  Retrieving Current Weather"
    const val FORECAST_WEATHER_ERROR_LOG = "Error Retrieving Forecast Weather"

    const val NOT_FOUND_ERROR_CODE = 404
    const val BAD_REQUEST_ERROR_CODE = 400

    const val CURRENT_HOUR_FLAG = 0L
    const val SUNRISE_FLAG = -100.0
    const val SUNSET_FLAG = -150.0

    const val DAILY_FORMAT_COMPARATOR = "yyyyMMdd"

    const val METRIC = "metric"
    const val IMPERIAL = "imperial"

    const val METRIC_WIND_SPEED = "Km/h"
    const val IMPERIAL_WIND_SPEED = "MPH"

    const val METRIC_PRECIPITATION = "mm"
    const val IMPERIAL_PRECIPITATION = "in"

    const val METRIC_VISIBILITY = "Km"
    const val IMPERIAL_VISIBILITY = "mi"

    const val HOUR_METRIC_FORMAT = "HH:mm"
    const val HOUR_IMPERIAL_FORMAT = "hh:mma"

    const val CURRENT_HOUR_FORMAT = "Now"
    const val SUNRISE_FORMAT = "Sunrise"
    const val SUNSET_FORMAT = "Sunset"

    const val HOURLY_METRIC_FORMAT = "HH"
    const val HOURLY_IMPERIAL_FORMAT = "ha"

    const val SUNRISE_SUNSET_METRIC_FORMAT = "HH:mm"
    const val SUNRISE_SUNSET_IMPERIAL_FORMAT = "h:mma"

    const val CURRENT_DAY_FORMAT = "Today"
    const val DAILY_FORMAT = "EEE"
}