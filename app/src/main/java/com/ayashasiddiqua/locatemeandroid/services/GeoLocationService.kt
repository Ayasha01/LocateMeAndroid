package com.ayashasiddiqua.locatemeandroid.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.ayashasiddiqua.locatemeandroid.models.GeoLocation
import com.ayashasiddiqua.locatemeandroid.providers.GeoLocationProvider
import com.ayashasiddiqua.locatemeandroid.utils.logD
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class GeoLocationService : Service() {
    private lateinit var geolocationProvider: GeoLocationProvider

    init {
        if (EventBus.getDefault().isRegistered(this).not()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        logD("Where", "GeoServiceHasBeenStarted")

        registerToLocationProvider()
        return START_STICKY
    }

    private fun registerToLocationProvider() {
        geolocationProvider = GeoLocationProvider(applicationContext)
    }

    @Subscribe
    fun onLocation(geoLocation: GeoLocation) {
        logD("OnNewLocation", geoLocation.toString())
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? = null
}
