package com.ayashasiddiqua.locatemeandroid.utils

import android.app.ActivityManager
import android.content.Context
import android.provider.Settings
import android.util.Log

fun getMapDefaultZoomLevel() = 18

fun getMinimumLocationUpdateInterval(): Long = 10000     // In Milli Seconds

fun getMinimumLocationUpdateDistance() = 10f     // In Meter

fun getMqttServerUri() = "tcp://iot.eclipse.org"

fun getDeviceId(context: Context): String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

fun getMqLocationEndpoint() = "/locateme/locations"

fun logD(tag: String, message: String) {
    Log.d(tag, message)
}

fun isServiceRunning(clazz: Any, context: Context): Boolean {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return activityManager.getRunningServices(Int.MAX_VALUE).any {
        it.service.className == clazz.javaClass.name
    }
}
