package com.ayashasiddiqua.locatemeandroid.ui.views

import com.arellomobile.mvp.MvpView
import com.ayashasiddiqua.locatemeandroid.models.GeoLocation

interface GeoLocationView : MvpView {
    fun onLocation(geoLocation: GeoLocation)
}
