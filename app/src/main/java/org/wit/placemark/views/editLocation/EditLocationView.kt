package org.wit.placemark.views.editLocation

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.addCallback
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.placemark.R
import org.wit.placemark.databinding.ActivityMapBinding
import org.wit.placemark.models.Location

class EditLocationView : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraMoveListener {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapBinding
    private var location = Location()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // location = intent.extras?.getParcelable("location",Location::class.java)!!
        location = intent.extras?.getParcelable<Location>("location")!!
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        onBackPressedDispatcher.addCallback(this ) {
            val resultIntent = Intent()
            resultIntent.putExtra("location", location)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val loc = LatLng(location.lat, location.lng)
        val options = MarkerOptions()
            .title("Placemark")
            .snippet("GPS : $loc")
            .draggable(true)
            .position(loc)

        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isZoomGesturesEnabled = true
        map.setOnMarkerClickListener(this)
        map.setOnMarkerDragListener(this)
        map.setOnCameraMoveListener(this)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
        map.addMarker(options)?.showInfoWindow()
    }

    override fun onMarkerDrag(marker: Marker) {
        location.lat = marker.position.latitude
        location.lng = marker.position.longitude
        location.zoom = map.cameraPosition.zoom
        val loc = LatLng(location.lat, location.lng)
        marker.snippet = "GPS : $loc"
    }

    override fun onMarkerDragEnd(marker: Marker) {
        location.lat = marker.position.latitude
        location.lng = marker.position.longitude
        location.zoom = map.cameraPosition.zoom
    }

    override fun onMarkerDragStart(marker: Marker) {
        location.lat = marker.position.latitude
        location.lng = marker.position.longitude
        location.zoom = map.cameraPosition.zoom
        val loc = LatLng(location.lat, location.lng)
        marker.snippet = "GPS : $loc"
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        location.lat = marker.position.latitude
        location.lng = marker.position.longitude
        location.zoom = map.cameraPosition.zoom
        val loc = LatLng(location.lat, location.lng)
        marker.snippet = "GPS : $loc"
        return false
    }

    override fun onCameraMove() {
        location.zoom = map.cameraPosition.zoom
    }
}