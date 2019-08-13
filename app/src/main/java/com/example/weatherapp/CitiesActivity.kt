package com.example.weatherapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

import com.example.weatherapp.models.City
import com.example.weatherapp.models.Coordinate
import com.example.weatherapp.models.Weather

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

import kotlinx.android.synthetic.main.activity_cities.*

import java.net.URL

class CitiesActivity : AppCompatActivity() {

    private var userLocation : Coordinate = Coordinate(0.0, 0.0)

    var citiesList : ArrayList<City> = ArrayList()

    //Debug TAGS
    private val DEBUG_USER_LOCATION = "User Location"
    private val DEBUG_TAG_CITIES = "Cities"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities)
        setSupportActionBar(toolbar)

        //Get Extra InformatioN
        intent.extras.get(USER_LAT).let { value -> userLocation.lat = value as Double }
        intent.extras.get(USER_LNG).let { value -> userLocation.lng = value as Double }

        searchCities()
    }

    private fun searchCities(){
        val API_URL = "https://api.openweathermap.org/data/2.5/find?lat=${userLocation.lat}&lon=${userLocation.lng}&cnt=15&APPID=fe04a6e4de6f7a1bdf2261c62fc2cf77"

        doAsync {
            val response = URL(API_URL).readText()

            uiThread {
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
                        "http://openweathermap.org/img/wn/${getWeather.getJSONObject(0).getString("icon")}@2x.png"
                    )

                    val city = City(
                        getCity.getString("name"),
                        getCity.getJSONObject("main").getDouble("temp_max"),
                        getCity.getJSONObject("main").getDouble("temp_min"),
                        weather
                    )

                    citiesList.add(city)
                }

                checkCities()

            }
        }

    }

    private fun checkCities() {
        for(city in citiesList) {
            val weather = city.weather
            Log.d(DEBUG_TAG_CITIES, "City Weather Icon: ${weather.icon}")
        }
    }


    companion object {
        val USER_LAT = "User_Latitude"
        val USER_LNG = "User_Longitude"
    }

}
