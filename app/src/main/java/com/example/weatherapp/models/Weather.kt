package com.example.weatherapp.models

import java.io.Serializable

class Weather(id: String?, main: String?, description: String?, icon: String?): Serializable {
    var id = id
    var main = main
    var description = description
    var icon = icon
}