package com.ayashasiddiqua.locatemeandroid.ui.activities

import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.MvpActivity
import com.ayashasiddiqua.locatemeandroid.R
import com.ayashasiddiqua.locatemeandroid.models.GeoLocation
import com.ayashasiddiqua.locatemeandroid.services.GeoLocationService
import com.ayashasiddiqua.locatemeandroid.services.MQConnectionService
import com.ayashasiddiqua.locatemeandroid.ui.views.GeoLocationView
import com.ayashasiddiqua.locatemeandroid.utils.getMapDefaultZoomLevel
import com.ayashasiddiqua.locatemeandroid.utils.isServiceRunning
import com.ayashasiddiqua.locatemeandroid.utils.logD
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.find
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem

class GeoLocationActivity : MvpActivity(), GeoLocationView, ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
    private lateinit var geolocationMap: MapView
    private lateinit var geolocationMapController: IMapController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geo_location)

        startServiceIfNotStarted()

        if (EventBus.getDefault().isRegistered(this).not()) {
            EventBus.getDefault().register(this)
        }

        geolocationMap = find(R.id.geolocationMap)
        geolocationMap.setTileSource(TileSourceFactory.MAPNIK)
        geolocationMap.setBuiltInZoomControls(true)
        geolocationMap.setMultiTouchControls(true)

        geolocationMapController = geolocationMap.controller
        geolocationMapController.setZoom(getMapDefaultZoomLevel())

        onLocation(GeoLocation())
    }

    @Subscribe
    override fun onLocation(geoLocation: GeoLocation) {
        logD("Where", "OnActivity = $geoLocation")

        val overlaySize = geolocationMap.overlays.size
        for (i in 0 until overlaySize) {
            geolocationMap.overlays.removeAt(i)
        }

        val geoPoint = GeoPoint(geoLocation.latitude, geoLocation.longitude)
        geolocationMapController.setCenter(geoPoint)

        val overlayItems = mutableListOf<OverlayItem>()
        overlayItems.add(OverlayItem("You", "Your current location", geoPoint))

        val itemizedOverlayWithFocus = ItemizedOverlayWithFocus<OverlayItem>(applicationContext, overlayItems, this)
        itemizedOverlayWithFocus.setFocusItemsOnTap(true)
        geolocationMap.overlays.add(itemizedOverlayWithFocus)
    }

    private fun startServiceIfNotStarted() {
        if (isServiceRunning(GeoLocationService::class.java, this).not()) {
            val serviceIntent = Intent(applicationContext, GeoLocationService::class.java)
            startService(serviceIntent)
        }
        if (isServiceRunning(MQConnectionService::class.java, this).not()) {
            val serviceIntent = Intent(applicationContext, MQConnectionService::class.java)
            startService(serviceIntent)
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean = true

    override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean = false
}
