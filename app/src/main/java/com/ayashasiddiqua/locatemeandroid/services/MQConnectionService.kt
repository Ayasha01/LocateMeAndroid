package com.ayashasiddiqua.locatemeandroid.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.eclipsesource.json.JsonObject
import com.ayashasiddiqua.locatemeandroid.models.GeoLocation
import com.ayashasiddiqua.locatemeandroid.utils.getDeviceId
import com.ayashasiddiqua.locatemeandroid.utils.getMqLocationEndpoint
import com.ayashasiddiqua.locatemeandroid.utils.getMqttServerUri
import com.ayashasiddiqua.locatemeandroid.utils.logD
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import kotlin.concurrent.thread

class MQConnectionService : Service(), MqttCallback {
    private lateinit var mqttClient: MqttAsyncClient

    init {
        if (EventBus.getDefault().isRegistered(this).not()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        logD("Where", "MQServiceHasBeenStarted")

        connectToServer()
        return START_STICKY
    }

    private fun connectToServer() {
        try {
            thread(start = true) {
                mqttClient = MqttAsyncClient(getMqttServerUri(), MqttAsyncClient.generateClientId(), MemoryPersistence())
                mqttClient.setCallback(this)
                mqttClient.connect()
            }
        } catch (ex: Exception) {
            logD("MQConnection", ex.message!!)
        }
    }

    private fun reconnectToServer() {
        try {
            mqttClient.reconnect()
        } catch (ex: Exception) {
            logD("MQReConnection", ex.message!!)
        }
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {

    }

    override fun connectionLost(cause: Throwable?) {
        logD("MQConnection", "Connection lost, ${cause!!.message}")

        thread(start = true) {
            Thread.sleep(5000)
            reconnectToServer()
        }
    }

    @Subscribe
    fun onLocation(geoLocation: GeoLocation) {
        logD("OnNewLocation", geoLocation.toString())

        val locationPack = JsonObject()
                .add("latitude", geoLocation.latitude)
                .add("longitude", geoLocation.longitude)
                .add("deviceId", getDeviceId(applicationContext))

        logD("MqttSend", locationPack.toString())

        if (isClientConnected()) {
            mqttClient.publish(getMqLocationEndpoint(), locationPack.toString().toByteArray(), 2, false)
        }
    }

    private fun isClientConnected(): Boolean {
        return true
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {

    }

    override fun onBind(intent: Intent): IBinder? = null
}
