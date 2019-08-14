package com.example.weatherapp.models

import java.io.Serializable

class City(name: String, temp_max: Double, temp_min: Double, weather: Weather) : Serializable {
    var name = name
    var temp_max = temp_max
    var temp_min = temp_min
    var weather = weather
}