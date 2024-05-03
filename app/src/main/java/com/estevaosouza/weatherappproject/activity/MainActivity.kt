package com.estevaosouza.weatherappproject.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.estevaosouza.weatherappproject.R
import com.estevaosouza.weatherappproject.adapter.DailyAdapter
import com.estevaosouza.weatherappproject.adapter.HourlyAdapter
import com.estevaosouza.weatherappproject.databinding.ActivityMainBinding
import com.estevaosouza.weatherappproject.model.ForecastWeather
import com.estevaosouza.weatherappproject.model.CurrentWeather
import com.estevaosouza.weatherappproject.model.Minutely
import com.estevaosouza.weatherappproject.util.Constants
import com.estevaosouza.weatherappproject.util.showMessage
import com.estevaosouza.weatherappproject.viewModel.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels()

    private val getCityWeather = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val data = activityResult.data
            if (data != null) {
                city = data.getStringExtra("cityName").toString()
            }
        }
    }

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var hourlyAdapter: HourlyAdapter

    private lateinit var dailyAdapter: DailyAdapter

    private var city = Constants.CITY_NAME

    private var unit = Constants.METRIC

    private var windSpeedUnit = Constants.METRIC_WIND_SPEED

    private var precipitationUnit = Constants.METRIC_PRECIPITATION

    private var visibilityUnit = Constants.METRIC_VISIBILITY

    private var latitude: Double? = null

    private var longitude: Double? = null

    private var timezone: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializeClickEvents()
    }

    override fun onStart() {
        super.onStart()
        getCurrentAndForecastWeather()
    }

    private fun initializeClickEvents() {
        with(binding) {
            // Change App Units Button
            fabUnits.setOnClickListener {
                temperatureUnitsSwitch()
                getCurrentAndForecastWeather()
            }

            // Show and Hide More Buttons Button
            fabMoreOptions.setOnClickListener {
                groupButtonsMenu.visibility = if (groupButtonsMenu.isVisible) {
                    View.INVISIBLE
                } else {
                    View.VISIBLE
                }
            }

            // Refresh Data Button
            fabRefreshData.setOnClickListener {
                groupButtonsMenu.visibility = View.INVISIBLE
                getCurrentAndForecastWeather()
                showMessage(Constants.WEATHER_UPDATED_MESSAGE)
            }

            // Open New Activity to Get New City Weather
            fabSearchCity.setOnClickListener {
                groupButtonsMenu.visibility = View.INVISIBLE
                getCityWeather.launch(
                    Intent(this@MainActivity, SearchCityActivity::class.java)
                )
            }
        }
    }

    private fun getCurrentAndForecastWeather() {
        CoroutineScope(Dispatchers.Main).launch {
            getCurrentWeather()
            getForecastWeather()
        }
    }

    private suspend fun getCurrentWeather() {

        // Make a Request for the First API (Current Weather)
        var response: Response<CurrentWeather>? = null
        try {
            response = weatherViewModel.loadCurrentWeather(city, unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("info_weather", Constants.CURRENT_WEATHER_ERROR_LOG)
        }

        // If Request is Successful
        if (response != null && response.isSuccessful) {
            val currentWeather = response.body()

            if (currentWeather != null) {
                // Set City's Coordinates and Time Zone
                latitude = currentWeather.coord.lat
                longitude = currentWeather.coord.lon
                timezone = currentWeather.timezone

                // Set Current City's Name
                binding.textCity.text = currentWeather.name
            }

        // If City is Not Found
        } else if (response?.code() == Constants.NOT_FOUND_ERROR_CODE) {
            showMessage(Constants.NOT_FOUND_MESSAGE_ERROR)

        // If was API Bad Request (Blank City Name)
        } else if (response?.code() == Constants.BAD_REQUEST_ERROR_CODE) {
            showMessage(Constants.BAD_REQUEST_MESSAGE_ERROR)
        }
    }

    private suspend fun getForecastWeather() {

        // Make a Request for the Second API (Forecast Weather)
        var response: Response<ForecastWeather>? = null
        try {
            response = weatherViewModel.loadForecastWeather(latitude!!, longitude!!, unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("info_weather", Constants.FORECAST_WEATHER_ERROR_LOG)
        }

        // If Request is Successful
        if (response != null && response.isSuccessful) {
            val forecastWeather = response.body()

            if (forecastWeather != null) {
                // Initialize All Layout's RecyclerViews
                initializeRecyclerViews(forecastWeather)

                with(binding) {
                    // Set Current Weather Description
                    textCurrentWeatherDescription.text = capitalizeEveryWord(forecastWeather.current.weather[0].description)

                    // Set Current Weather Temperature
                    val currentTemperature = "${forecastWeather.current.temp.roundToInt()}º"
                    textCurrentTemperature.text = currentTemperature

                    // Set Current Weather Maximum Temperature
                    val maxTemperature = "${forecastWeather.daily[0].temp.max.roundToInt()}º"
                    textCurrentMaxTemperature.text = maxTemperature

                    // Set Current Weather Minimum Temperature
                    val minTemperature = "${forecastWeather.daily[0].temp.min.roundToInt()}º"
                    textCurrentMinTemperature.text = minTemperature

                    // Set Current Weather Wind Speed
                    textWindResult.text = setWindSpeed(forecastWeather.current.windSpeed)

                    // Set Current Weather Humidity
                    val currentHumidity = "${forecastWeather.current.humidity}%"
                    textHumidityResult.text = currentHumidity

                    // Set Current Weather Feels Like
                    val currentFeelsLike = "${forecastWeather.current.feelsLike.roundToInt()}º"
                    textFeelsLikeResult.text = currentFeelsLike

                    // Set Current Weather UV Index
                    textUvIndexResult.text = forecastWeather.current.uvi.roundToInt().toString()

                    // Set Current Weather Sunrise
                    textSunriseResult.text = convertEpochToTime(forecastWeather.current.sunrise)

                    // Set Current Weather Sunset
                    textSunsetResult.text = convertEpochToTime(forecastWeather.current.sunset)

                    // Set Current Weather Precipitation
                    textPrecipitationResult.text = setPrecipitation(forecastWeather.minutely)

                    // Set Current Weather Visibility
                    textVisibilityResult.text = setVisibility(forecastWeather.current.visibility)
                }
            }
        }
    }

    private fun initializeRecyclerViews(forecastWeather: ForecastWeather) {
        // Initialize Hourly Weather RecyclerView
        hourlyAdapter = HourlyAdapter(unit, timezone!!)
        binding.rvHourly.adapter = hourlyAdapter
        binding.rvHourly.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.HORIZONTAL,
            false
        )
        // Populate Hourly Weather RecyclerView List
        hourlyAdapter.populateList(forecastWeather.daily, forecastWeather.hourly)

        // Initialize Daily Weather RecyclerView
        dailyAdapter = DailyAdapter()
        binding.rvDaily.adapter = dailyAdapter
        binding.rvDaily.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        // Populate Daily Weather RecyclerView List
        dailyAdapter.populateList(forecastWeather.daily)
    }

    private fun temperatureUnitsSwitch() {
        // Change Button Layout and Weather Units
        when (unit) {
            Constants.METRIC -> {
                binding.fabUnits.setImageResource(R.drawable.ic_fahrenheit_24)
                unit = Constants.IMPERIAL
                windSpeedUnit = Constants.IMPERIAL_WIND_SPEED
                precipitationUnit = Constants.IMPERIAL_PRECIPITATION
                visibilityUnit = Constants.IMPERIAL_VISIBILITY
            }

            Constants.IMPERIAL -> {
                binding.fabUnits.setImageResource(R.drawable.ic_celsius_24)
                unit = Constants.METRIC
                windSpeedUnit = Constants.METRIC_WIND_SPEED
                precipitationUnit = Constants.METRIC_PRECIPITATION
                visibilityUnit = Constants.METRIC_VISIBILITY
            }
        }
    }

    private fun setWindSpeed(windSpeed: Double) : String {
        return if (unit == Constants.METRIC) {
            "${(windSpeed*3.6).roundToInt()} $windSpeedUnit"
        } else {
            "${windSpeed.roundToInt()} $windSpeedUnit"
        }
    }

    private fun convertEpochToTime(epoch: Long) : String {
        return if (unit == Constants.METRIC) {
            SimpleDateFormat(Constants.HOUR_METRIC_FORMAT, Locale.ENGLISH).format(Date((epoch + timezone!!)*1000))
        } else {
            SimpleDateFormat(Constants.HOUR_IMPERIAL_FORMAT, Locale.ENGLISH).format(Date((epoch + timezone!!)*1000))
        }
    }

    private fun setPrecipitation(minutely: List<Minutely>) : String {
        // Check First If Minutely List is Not Null
        val precipitation = if (minutely != null) {
            minutely[0].precipitation
        } else {
            0.0
        }

        // Set Precipitation According to Unit
        return if (unit == Constants.METRIC) {
            "${precipitation.roundToInt()} $precipitationUnit"
        } else {
            "${(precipitation*0.0393700787).roundToInt()} $precipitationUnit"
        }
    }

    private fun setVisibility(visibility: Int) : String {
        return if (unit == Constants.METRIC) {
            "${visibility/1000} $visibilityUnit"
        } else {
            "${(visibility*0.000621371192).roundToInt()} $visibilityUnit"
        }
    }

    private fun capitalizeEveryWord(text: String) : String {
        return text.split(" ").joinToString(" ") { it.replaceFirstChar(Char::titlecase) }
    }
}