package com.ayashasiddiqua.locatemeandroid.models

class GeoLocation constructor(var latitude: Double = 23.756, var longitude: Double = 90.370) {
    override fun toString(): String = "Lat = $latitude, Lon = $longitude"
}
