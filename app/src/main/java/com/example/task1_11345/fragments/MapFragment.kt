package com.example.task1_11345.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.task1_11345.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textview.MaterialTextView

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map_LBL_title: MaterialTextView
    private var googleMap: GoogleMap? = null
    private var pendingLatLng: LatLng? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.map_fragment, container, false)
        map_LBL_title = view.findViewById(R.id.map_LBL_title)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap

        val defaultLatLng = LatLng(32.0853, 34.7818)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 12f))

        pendingLatLng?.let {
            moveToHighScoreLocation(it.latitude, it.longitude)
            pendingLatLng = null
        }
    }


    fun moveToHighScoreLocation(lat: Double, lon: Double) {
        val latLng = LatLng(lat, lon)
        googleMap?.addMarker(MarkerOptions().position(latLng).title("🎯 High Score Location"))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

        googleMap?.addMarker(MarkerOptions().position(latLng).title("🎯 High Score Location"))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

        map_LBL_title.text = buildString {
            append("🎯\n")
            append(lat)
            append(",\n")
            append(lon)
        }
    }

}