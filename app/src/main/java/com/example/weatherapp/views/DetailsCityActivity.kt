package com.example.weatherapp.views

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.example.weatherapp.R
import com.example.weatherapp.models.City
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_details_city.*

class DetailsCityActivity : AppCompatActivity() {

    //City Selected from User
    private lateinit var city: City

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_city)

        setSupportActionBar(toolbarDetailsCity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Getting City Selected by User as Extras because always need to be selected from CitiesActivity
        intent.extras?.getSerializable(CITY).let { value -> city = value as City }
    }

    override fun onStart() {
        super.onStart()
        setupCity()
    }

    private fun setupCity(){
        txtCityName.text = city.name
        txtCityTempMax.text = city.temp_max.toString()
        txtCityTempMin.text = city.temp_min.toString()
        txtCityDescription.text = city.weather.description
        Picasso.get().load(city.weather.icon).into(imgWeatherCity)
    }

    companion object {
        val CITY = "Selected_City"
    }
}
