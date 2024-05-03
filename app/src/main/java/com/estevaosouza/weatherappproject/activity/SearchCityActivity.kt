package com.estevaosouza.weatherappproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.estevaosouza.weatherappproject.databinding.ActivitySearchCityBinding

class SearchCityActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySearchCityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializeClickEvents()
    }

    private fun initializeClickEvents() {
        with(binding) {
            // Search City Button
            btnSearch.setOnClickListener {
                // Send Information Back to Parent Activity
                val intent = Intent()
                intent.putExtra("cityName", editTextCity.text.toString())
                setResult(RESULT_OK, intent)
                finish()
            }

            // Close Activity Button (Return to Parent Activity)
            fabBack.setOnClickListener {
                finish()
            }
        }

    }
}