package com.estevaosouza.weatherappproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.estevaosouza.weatherappproject.R
import com.estevaosouza.weatherappproject.databinding.ItemDailyBinding
import com.estevaosouza.weatherappproject.model.Daily
import com.estevaosouza.weatherappproject.util.Constants
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class DailyAdapter : Adapter<DailyAdapter.DailyViewHolder>() {

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
        "50n" to R.drawable.ic_50
        )

    inner class DailyViewHolder(
        private val binding: ItemDailyBinding
    ) : ViewHolder(binding.root) {

        fun bind(daily: Daily) {
            with(binding) {
                // Set Respective Weather Icon
                imgDailyWeather.setImageResource(weatherIconsHashMap[daily.weather[0].icon]!!)

                // Set Respective Weather Day of the Week
                textDay.text = convertEpochToDayOfWeek(daily.dt)

                // Set Respective Weather Minimum Temperature
                val temperatureMin = daily.temp.min.roundToInt()
                val temperatureMinText = "${temperatureMin}º"
                textDayMinTemperature.text = temperatureMinText

                // Set Respective Weather Maximum Temperature
                val temperatureMax = daily.temp.max.roundToInt()
                val temperatureMaxText = "${temperatureMax}º"
                textDayMaxTemperature.text = temperatureMaxText

                // Set Respective Temperature Progress Bar
                val progressBarMaxWidth = highestTemperature!! - lowestTemperature!!
                val progressBarStart = (temperatureMin-lowestTemperature!!).toBigDecimal().divide(
                    progressBarMaxWidth.toBigDecimal(), 2, RoundingMode.HALF_UP
                )
                val progressBarEnd = (highestTemperature!!-temperatureMax).toBigDecimal().divide(
                    progressBarMaxWidth.toBigDecimal(), 2, RoundingMode.HALF_UP
                )
                progressBarVariable.setPadding(
                    (progressBarStart.multiply(BigDecimal(403))).toInt(),0,
                    (progressBarEnd.multiply(BigDecimal(403))).toInt(),0
                )
            }
        }
    }

    // List with RecyclerView Data
    private var dailyList = emptyList<Daily>()

    // Values of Highest and Lowest Temperatures from List
    private var highestTemperature: Int? = null
    private var lowestTemperature: Int? = null

    fun populateList(list: List<Daily>) {
        dailyList = list

        // Set Highest and Lowest Temperatures
        highestTemperature = dailyList.maxOf { it.temp.max }.roundToInt()
        lowestTemperature = dailyList.minOf { it.temp.min }.roundToInt()
    }

    // Create the Visualization
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = ItemDailyBinding.inflate(inflater, parent, false)

        return DailyViewHolder(itemView)
    }

    // Vinculate Each List Item for Visualization
    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val daily = dailyList[position]
        holder.bind(daily)
    }

    // Return Data List Size
    override fun getItemCount(): Int {
        return dailyList.size
    }

    private fun convertEpochToDayOfWeek(epoch: Long) : String {
        val current = SimpleDateFormat(Constants.DAILY_FORMAT_COMPARATOR, Locale.ENGLISH).format(Date())
        val epochDate = Date(epoch*1000)
        // Set as "Today" When is Current Day
        return if (current != SimpleDateFormat(Constants.DAILY_FORMAT_COMPARATOR, Locale.ENGLISH).format(epochDate)) {
            SimpleDateFormat(Constants.DAILY_FORMAT, Locale.ENGLISH).format(epochDate)
        } else {
            Constants.CURRENT_DAY_FORMAT
        }
    }
}