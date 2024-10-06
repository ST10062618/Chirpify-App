package com.authorisation.chirpify

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HotspotsList : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var hotspotsListView: ListView
    private lateinit var refreshButton: Button
    private lateinit var hotspotsAdapter: ArrayAdapter<Hotspot>
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotspots_list)

        hotspotsListView = findViewById(R.id.hotspotsListView)
        refreshButton = findViewById(R.id.refreshEBirdHotspotsButton)

        hotspotsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, emptyList())
        hotspotsListView.adapter = hotspotsAdapter

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        refreshButton.setOnClickListener {
            fetchHotspots()
        }

        // Initialize the map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Get the user's current location
        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                currentLocation = location
                if (location != null) {
                    // Move the camera to the user's location
                    val userLatLng = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
                }
            }
        }
    }

    private fun fetchHotspots() {
        val latitude = currentLocation?.latitude ?: -26.2041 // Default if no location
        val longitude = currentLocation?.longitude ?: 28.0473
        val maxDistance = 10 // Replace with actual max distance from settings

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = HotspotsRepository(this@HotspotsList)
                val hotspots = repository.fetchEBirdHotspots(latitude, longitude, maxDistance)
                withContext(Dispatchers.Main) {
                    handleHotspotsResponse(hotspots)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@HotspotsList, "Error fetching hotspots: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun handleHotspotsResponse(hotspots: List<Hotspot>) {
        hotspotsAdapter.clear()
        hotspotsAdapter.addAll(hotspots)
        hotspotsAdapter.notifyDataSetChanged()

        if (hotspots.isEmpty()) {
            Toast.makeText(this, "No hotspots found", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Found ${hotspots.size} hotspots", Toast.LENGTH_SHORT).show()
            displayHotspotsOnMap(hotspots)
        }
    }

    private fun displayHotspotsOnMap(hotspots: List<Hotspot>) {
        hotspots.forEach { hotspot ->
            val position = LatLng(hotspot.latitude, hotspot.longitude)
            mMap.addMarker(MarkerOptions().position(position).title(hotspot.name))
        }

        // Display the user's location
        currentLocation?.let {
            val userLatLng = LatLng(it.latitude, it.longitude)
            mMap.addMarker(MarkerOptions().position(userLatLng).title("Your Location"))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getCurrentLocation() // Get the current location once the map is ready
    }
}

