package com.example.weatherapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Button

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    //Location Permission
    private val MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 101

    //User Location
    var lat: Double = 0.0
    var lng: Double = 0.0

    var posLat: Double = 0.0
    var posLng: Double = 0.0

    //Debug Tags
    private var DEBUG_LOCATION_TAG = "User Location"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        //Initialize FusedLocationProvider
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        //Check Location Permission
        checkUserLocationPermission()

        val btnSearch = findViewById(R.id.searchBtn) as Button
        btnSearch.setOnClickListener {
            navigateSearchPage(posLat, posLng)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            setupUserLocation()
        }
    }

    private fun setupUserLocation(){
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            lat = location.latitude
            lng = location.longitude

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val userLocation = LatLng(lat, lng)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12.0f))

        mMap.setOnMapClickListener {
            mMap.addMarker(MarkerOptions().position(it))
            posLat = it.latitude
            posLng = it.longitude
        }
    }


    private fun checkUserLocationPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION)

        } else {
            setupUserLocation()
        }
    }

    private fun navigateSearchPage(lat: Double, lng: Double){
        //Setup Intent to Navigate passing User Location
        val citiesIntent = Intent(this, CitiesActivity::class.java)
        citiesIntent.putExtra(USER_LAT, lat)
        citiesIntent.putExtra(USER_LNG, lng)
        //Start CitiesActivity
        startActivity(citiesIntent)
    }

    companion object {
        val USER_LAT = "User_Latitude"
        val USER_LNG = "User_Longitude"
    }
}
