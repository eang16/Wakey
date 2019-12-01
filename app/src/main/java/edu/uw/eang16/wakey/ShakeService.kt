package edu.uw.eang16.wakey

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import kotlin.math.sqrt

class ShakeService: Service(), SensorEventListener {
    private lateinit var mSensorManager:SensorManager
    private lateinit var mAccelerometer:Sensor
    private var mAccel:Float = 0.toFloat() // acceleration apart from gravity
    private var mAccelCurrent:Float = 0.toFloat() // current acceleration including gravity
    private var mAccelLast:Float = 0.toFloat() // last acceleration including gravity
    private var mShakeCount:Int = 0

    override fun onBind(intent:Intent): IBinder? {
        return null
    }
    override fun onStartCommand(intent:Intent, flags:Int, startId:Int):Int {
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mSensorManager.registerListener(this, mAccelerometer,
            SensorManager.SENSOR_DELAY_UI, Handler())
        return START_STICKY
    }
    override fun onAccuracyChanged(sensor:Sensor, accuracy:Int) {}

    override fun onSensorChanged(event:SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        this.mAccelLast = mAccelCurrent
        mAccelCurrent = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        val delta = mAccelCurrent - mAccelLast
        mAccel = mAccel * 0.9f + delta
        if (mAccel > 12) {
            mShakeCount++
            ShakeServiceActivity.shakeText.text = "$mShakeCount%"
            if (mShakeCount >= 100) {
                ShakeServiceActivity.shakeText.text = "100%"
            }
        }
    }
}

