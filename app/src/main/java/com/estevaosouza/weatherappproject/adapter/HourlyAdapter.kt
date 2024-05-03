package com.estevaosouza.weatherappproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.estevaosouza.weatherappproject.R
import com.estevaosouza.weatherappproject.databinding.ItemHourlyBinding
import com.estevaosouza.weatherappproject.model.Daily
import com.estevaosouza.weatherappproject.model.Hourly
import com.estevaosouza.weatherappproject.model.Weather
import com.estevaosouza.weatherappproject.util.Constants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class HourlyAdapter(
    private val unit: String,
    private val timezoneOffset: Long
) : Adapter<HourlyAdapter.HourlyViewHolder>() {

    val weatherIconsHashMap = hashMapOf(
        "01d" to R.drawable.ic_01d,
        "01n" to R.drawable.ic_01n,
        "02d" to R.drawable.ic_02d,
        "02n" to R.drawable.ic_02n,
        "03d" to R.drawable.ic_03,
        "03n" to R.drawable.ic_03,
        "04d" to R.drawable.ic_04,
        "04n" to R.drawable.ic_04,
        "09d" to R.drawable.ic_09d,
        "09n" to R.drawable.ic_09n,
        "10d" to R.drawable.ic_10,
        "10n" to R.drawable.ic_10,
        "11d" to R.drawable.ic_11,
        "11n" to R.drawable.ic_11,
        "13d" to R.drawable.ic_13,
        "13n" to R.drawable.ic_13,
        "50d" to R.drawable.ic_50,
        "50n" to R.drawable.ic_50,
        "Sunrise" to R.drawable.ic_sunrise,
        "Sunset" to R.drawable.ic_sunset
        )

    inner class HourlyViewHolder(
        private val binding: ItemHourlyBinding
    ) : ViewHolder(binding.root) {

        fun bind(hourly: Hourly) {
            with(binding) {
                // Set Respective Weather Hour
                textHour.text = convertEpochToHour(hourly.dt, hourly.temp)

                // Set Respective Weather Icon
                imgHourlyWeather.setImageResource(weatherIconsHashMap[hourly.weather[0].icon]!!)

                // Set Respective Weather Temperature
                textTemperature.text = setTemperature(hourly.temp)
            }
        }
    }

    // List with RecyclerView Data
    private var hourlyList = emptyList<Hourly>()

    fun populateList(dayList: List<Daily>, hourList: List<Hourly>) {

        val mutableList: MutableList<Hourly> = mutableListOf()

        // Add the Hours of Sunrise and Sunset for the Respective Period
        val hourlyRange = hourList[0].dt .. hourList[hourList.size-1].dt
        dayList.forEach { day ->
            if (hourlyRange.contains(day.sunrise)) {
                mutableList.add(buildHourlyObject(day.sunrise, Constants.SUNRISE_FLAG, Constants.SUNRISE_FORMAT))
            }

            if (hourlyRange.contains(day.sunset)) {
                mutableList.add(buildHourlyObject(day.sunset, Constants.SUNSET_FLAG, Constants.SUNSET_FORMAT))
            }
        }

        // Add the Remaining Hours
        mutableList.addAll(hourList)

        // Sort List
        mutableList.sortBy { it.dt }

        // Edit First Element from List (Add Current Hour Flag)
        val firstHour = mutableList[0]
        mutableList.remove(firstHour)
        mutableList.add(
            0,
            buildHourlyObject(Constants.CURRENT_HOUR_FLAG, firstHour.temp, firstHour.weather[0].icon)
        )

        // Add Result List to RecyclerView List
        hourlyList = mutableList
    }

    // Create the Visualization
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = ItemHourlyBinding.inflate(inflater, parent, false)

        return HourlyViewHolder(itemView)
    }

    // Vinculate Each List Item for Visualization
    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val hourly = hourlyList[position]
        holder.bind(hourly)
    }

    // Return Data List Size
    override fun getItemCount(): Int {
        return hourlyList.size
    }

    private fun buildHourlyObject(dt: Long, temp: Double, weatherIcon: String): Hourly {
        return Hourly(dt, temp, listOf( Weather("", weatherIcon, "")))
    }

    private fun setTemperature(temperature: Double) : String {
        return if (temperature.equals(Constants.SUNRISE_FLAG)) {
            Constants.SUNRISE_FORMAT
        } else if (temperature.equals(Constants.SUNSET_FLAG)) {
            Constants.SUNSET_FORMAT
        } else {
            "${(temperature.roundToInt())}º"
        }
    }

    private fun convertEpochToHour(epoch: Long, temperature: Double) : String {
        // Distinguish the Normal Hours from Current Hour, Sunrise and Sunset
        return if (epoch == Constants.CURRENT_HOUR_FLAG) {
            Constants.CURRENT_HOUR_FORMAT
        } else if (temperature.equals(Constants.SUNRISE_FLAG) || temperature.equals(Constants.SUNSET_FLAG)) {
            if (unit == Constants.METRIC) {
                setHour(epoch, Constants.SUNRISE_SUNSET_METRIC_FORMAT)
            } else {
                setHour(epoch, Constants.SUNRISE_SUNSET_IMPERIAL_FORMAT)
            }
        } else {
            if (unit == Constants.METRIC) {
                "${setHour(epoch, Constants.HOURLY_METRIC_FORMAT)} h"
            } else {
                setHour(epoch, Constants.HOURLY_IMPERIAL_FORMAT)
            }
        }
    }

    private fun setHour(epoch: Long, hourFormat: String) : String {
        return SimpleDateFormat(hourFormat, Locale.ENGLISH).format(Date((epoch + timezoneOffset)*1000))
    }
}