package com.example.weatherapp.views

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Button
import android.widget.Toast

import com.example.weatherapp.R
import com.example.weatherapp.models.Coordinate
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.inlocomedia.android.core.permissions.PermissionResult
import com.inlocomedia.android.core.permissions.PermissionsListener
import com.inlocomedia.android.engagement.InLocoEngagement
import com.inlocomedia.android.engagement.request.FirebasePushProvider

import org.jetbrains.anko.defaultSharedPreferences


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    //Shared Preferences
    private lateinit var preferences: SharedPreferences

    //Location Permission
    private val MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 101

    //User Location
    private lateinit var userLocation : Coordinate
    private lateinit var posLocation : Coordinate

    //Debug TAGS
    private var DEBUG_INLOCO = "InLoco"
    private var DEBUG_FIREBASE = "Firebase"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        //Initialize variables
        userLocation = Coordinate(0.0, 0.0)
        posLocation = Coordinate(0.0, 0.0)

        //Initialize SharedPreference
        preferences = defaultSharedPreferences

        //Initialize FusedLocationProvider
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        //InLoco Permissions
        setupRequestPermission()

        //Check Location Permission
        checkUserLocationPermission()

        //Notification
        setupTokenNotification()

        val btnSearch : Button = findViewById(R.id.searchBtn)
        btnSearch.setOnClickListener {
            navigateSearchPage()
        }

    }

    private fun setupRequestPermission(){
        val askIfDenied = true // Will prompt the user if he has previously denied the permission

        InLocoEngagement.requestPermissions(this, REQUIRED_PERMISSION, askIfDenied, object : PermissionsListener {

            override fun onPermissionRequestCompleted(permissionResult: HashMap<String, PermissionResult>) {
                if (permissionResult[Manifest.permission.ACCESS_FINE_LOCATION]!!.isAuthorized()) {
                    // Permission enabled
                    Log.d(DEBUG_INLOCO, "Permissions enable")
                }
            }
        })
    }

    private fun setupTokenNotification(){
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(DEBUG_FIREBASE, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val firebaseToken = task.result?.token

                if(firebaseToken != null && !firebaseToken.isEmpty()) {
                    val pushProvider = FirebasePushProvider.Builder()
                        .setFirebaseToken(firebaseToken)
                        .build()

                    InLocoEngagement.setPushProvider(this, pushProvider)
                }
            })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            setupUserLocation()
        }
    }

    private fun setupUserLocation(){
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            userLocation = Coordinate(location.latitude, location.longitude)

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val userLocation = LatLng(userLocation.lat, userLocation.lng)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12.0f))

        mMap.setOnMapClickListener {
            mMap.clear() //Remove all marker already set
            mMap.addMarker(MarkerOptions().position(it))
            posLocation = Coordinate(it.latitude, it.longitude)
        }
    }


    private fun checkUserLocationPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION)

        } else {
            //Permission has already been GRANTED
            setupUserLocation()
        }
    }

    private fun navigateSearchPage(){

        if(posLocation.lat == 0.0 && posLocation.lng == 0.0) {
            //Force user to select a position on Map
            val toast : Toast = Toast.makeText(this, "Please, click on map to select a position", Toast.LENGTH_SHORT)
            toast.show()
        } else {
            //Using SharedPreference as safe storage and by-pass to CitiesActivity
            saveSharedPreference()

            //Setup Intent to Navigate passing User Location
            val citiesIntent = Intent(this, CitiesActivity::class.java)
            //Start CitiesActivity
            startActivity(citiesIntent)
        }
    }

    private fun saveSharedPreference() {
        val gson = Gson() //Initialize Gson (convert to JSON)
        val editor : SharedPreferences.Editor = preferences.edit()
        editor.clear() //Clean all cache from SharedPrefence

        val statusLocation = gson.toJson(posLocation) as String
        editor.putString(PREFERENCES, statusLocation).apply()
    }

    companion object {
        val PREFERENCES = "User_Position"
        private val REQUIRED_PERMISSION = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}
