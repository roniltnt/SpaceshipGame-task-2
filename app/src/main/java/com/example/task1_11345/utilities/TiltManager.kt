package com.example.task1_11345.utilities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.task1_11345.interfaces.TiltCallBack

class TiltManager(context: Context, private var tiltCallback: TiltCallBack?) {

    private val sensorManager = context
        .getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val sensor = sensorManager
        .getDefaultSensor(Sensor.TYPE_ACCELEROMETER) as Sensor

    private lateinit var sensorEventListener: SensorEventListener

    private var lastY: Float? = null

    private var timestamp: Long = 0L

    init {
        initEventListener()
    }

    private fun initEventListener() {
        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                calculateTilt(x, y)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        }
    }

    private fun calculateTilt(x: Float, y: Float) {
        if (System.currentTimeMillis() - timestamp >= 500) {
            timestamp = System.currentTimeMillis()

            tiltCallback?.moveBySensor(x)
            val shouldUpdate = lastY == null || kotlin.math.abs(lastY!! - y) > 0.5
            if (shouldUpdate) {
                lastY = y
                tiltCallback?.changeRockSpeed(y)
            }

        }
    }

    fun start() {
        sensorManager
            .registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
    }

    fun stop() {
        sensorManager
            .unregisterListener(
                sensorEventListener,
                sensor
            )
    }
}