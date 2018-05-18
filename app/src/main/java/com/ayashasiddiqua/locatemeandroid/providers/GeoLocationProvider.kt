package com.ayashasiddiqua.locatemeandroid.providers

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.ayashasiddiqua.locatemeandroid.models.GeoLocation
import com.ayashasiddiqua.locatemeandroid.utils.getMinimumLocationUpdateDistance
import com.ayashasiddiqua.locatemeandroid.utils.getMinimumLocationUpdateInterval
import com.ayashasiddiqua.locatemeandroid.utils.logD
import org.greenrobot.eventbus.EventBus

class GeoLocationProvider constructor(var context: Context) : LocationListener {
    private var locationManager: LocationManager? = null

    init {
        init()
    }

    private fun init() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, getMinimumLocationUpdateInterval(),
                getMinimumLocationUpdateDistance(), this)
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

    }

    override fun onProviderEnabled(p0: String?) {

    }

    override fun onProviderDisabled(p0: String?) {

    }

    override fun onLocationChanged(p0: Location?) {
        if (p0 != null) {
            val geoLocation = GeoLocation()
            geoLocation.latitude = p0.latitude
            geoLocation.longitude = p0.longitude

            EventBus.getDefault().post(geoLocation)
            logD("OnLocationUpdate", geoLocation.toString())
        }
    }
}
