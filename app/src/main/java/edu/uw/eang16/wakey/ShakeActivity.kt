package edu.uw.eang16.wakey

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_shake.*
import kotlin.math.sqrt

class ShakeActivity : AppCompatActivity(), WakeyAlarm, SensorEventListener {
    lateinit var data: AlarmData

    private lateinit var mSensorManager: SensorManager
    private lateinit var mAccelerometer: Sensor
    private var mAccel:Float = 0.toFloat() // acceleration apart from gravity
    private var mAccelCurrent:Float = 0.toFloat() // current acceleration including gravity
    private var mAccelLast:Float = 0.toFloat() // last acceleration including gravity
    private var mShakeCount:Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shake)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        data = this.intent!!.getParcelableExtra("data")
        startAlarm(data, this)
        shakeText = findViewById(R.id.shakePercentage)

        snooze.setOnClickListener{
            if (snoozeAlarm(data, this)) {
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI, Handler())
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this, mAccelerometer)
    }

    override fun onAccuracyChanged(sensor:Sensor, accuracy:Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        this.mAccelLast = mAccelCurrent
        mAccelCurrent = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        val delta = mAccelCurrent - mAccelLast
        mAccel = mAccel * 0.9f + delta
        if (mAccel > 12) {
            mShakeCount++
            shakeText!!.text = "$mShakeCount%"
            if (mShakeCount >= 100) {
                shakeText!!.text = "100%"
                Toast.makeText(this, "Task solved! Alarm dismissed!", Toast.LENGTH_SHORT).show()
                stopAlarm(data, this)
                finish()
            }
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var shakeText: TextView? = null
    }

}
