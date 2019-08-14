package com.example.weatherapp

import android.app.Application
import com.inlocomedia.android.engagement.InLocoEngagement
import com.inlocomedia.android.engagement.InLocoEngagementOptions

class WeatherApp: Application() {

    override fun onCreate() {
        super.onCreate()

        // Set initialization options
        val options = InLocoEngagementOptions.getInstance(this)

        // The App ID you obtained in the dashboard
        options.applicationId = "2377d605-1249-4ed0-aceb-1e8d2f35c352"

        // Verbose mode; enables SDK logging, defaults to true.
        // Remember to set to false in production builds.
        options.isLogEnabled = true

        //Initialize the SDK
        InLocoEngagement.init(this, options)
    }
}