package com.example.weatherapp.views

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar

import com.example.weatherapp.R
import com.example.weatherapp.adapters.CityAdapter
import com.example.weatherapp.models.City
import com.example.weatherapp.models.Coordinate
import com.example.weatherapp.models.Weather
import com.google.gson.Gson

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import org.jetbrains.anko.defaultSharedPreferences

import kotlinx.android.synthetic.main.activity_cities.*

import java.net.URL

class CitiesActivity : AppCompatActivity() {

    //User Location
    private var userLocation : Coordinate = Coordinate(0.0, 0.0)

    //UI Components
    private lateinit var citiesRecycleView: RecyclerView
    private lateinit var progressBar : ProgressBar

    //Shared Preferences
    private lateinit var preferences: SharedPreferences

    //Cities
    var citiesList : MutableList<City> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities)

        //Setup Toolbar to enable back button
        setSupportActionBar(toolbarCities)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Shared Preference
        preferences = defaultSharedPreferences

        //Getting UserLocation from SharedPreference
        userLocation = getSharedPreferences()

        progressBar = findViewById(R.id.progressBarCities)
        citiesRecycleView = findViewById(R.id.recycleViewCities)

        //Setup RecyclerView
        citiesRecycleView.apply {
            val layoutManager = LinearLayoutManager(this@CitiesActivity)
            citiesRecycleView.layoutManager = layoutManager
        }
    }

    override fun onStart() {
        super.onStart()
        progressBar.visibility = View.VISIBLE
        searchCities()
    }

    private fun searchCities(){
        val API_URL = "https://api.openweathermap.org/data/2.5/find?lat=${userLocation.lat}&lon=${userLocation.lng}&cnt=15&APPID=fe04a6e4de6f7a1bdf2261c62fc2cf77"


        doAsync {
            val response = URL(API_URL).readText()

            val jsonResponse = JSONObject(response)
            val cities = jsonResponse.getJSONArray("list")

            val citiesLength = cities.length()

            for(i in 0 until citiesLength) {
                val getCity = cities.getJSONObject(i)

                //Creating Cities List
                val getWeather = getCity.getJSONArray("weather")
                val weather = Weather(
                    getWeather.getJSONObject(0).getString("id"),
                    getWeather.getJSONObject(0).getString("main"),
                    getWeather.getJSONObject(0).getString("description"),
                    "https://openweathermap.org/img/wn/${getWeather.getJSONObject(0).getString("icon")}@2x.png"
                )

                val city = City(
                    getCity.getString("name"),
                    getCity.getJSONObject("main").getDouble("temp_max"),
                    getCity.getJSONObject("main").getDouble("temp_min"),
                    weather
                )

                citiesList.add(city)
            }

            uiThread {

                recycleViewCities.adapter = CityAdapter(citiesList, it)
                progressBar.visibility = View.GONE

            }
        }

    }

    private fun getSharedPreferences(): Coordinate {
        val gson = Gson()
        val preferencePosition = preferences.getString(PREFERENCES, "0")
        val statusPosition = gson.fromJson(preferencePosition, Coordinate::class.java)

        return statusPosition
    }

    companion object {
        val PREFERENCES = "User_Position"
    }

}
