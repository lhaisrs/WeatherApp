package com.example.weatherapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import org.jetbrains.anko.alert

import kotlinx.android.synthetic.main.activity_cities.*
import org.json.JSONArray

import java.net.URL

class CitiesActivity : AppCompatActivity() {

    private var userLat = 0.0
    private var userLng = 0.0

    //Debug TAGS
    private val DEBUG_USER_LOCATION = "User Location"
    private val DEBUG_TAG_CITIES = "Cities"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities)
        setSupportActionBar(toolbar)

        //Get Extra Information
        userLat = intent.extras.get(USER_LAT) as Double
        userLng = intent.extras.get(USER_LNG) as Double

        searchCities()
    }

    private fun searchCities(){
        val API_URL = "https://api.openweathermap.org/data/2.5/find?lat=${userLat}&lon=${userLng}&cnt=15&APPID=fe04a6e4de6f7a1bdf2261c62fc2cf77"

        doAsync {
            val response = URL(API_URL).readText()

            uiThread {
                val jsonResponse = JSONObject(response)
                val cities = jsonResponse.getJSONArray("list")
                val cityName = cities.getJSONObject(0).getString("name")

                Log.d(DEBUG_TAG_CITIES, "Name: ${cityName}")

            }
        }

    }


    companion object {
        val USER_LAT = "User_Latitude"
        val USER_LNG = "User_Longitude"
    }

}
