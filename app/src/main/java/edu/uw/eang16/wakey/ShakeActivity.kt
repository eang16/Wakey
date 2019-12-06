package edu.uw.eang16.wakey

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Handler
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_shake.*
import kotlin.math.sqrt

class ShakeActivity : AppCompatActivity(), WakeyAlarm, SensorEventListener {
    lateinit var data: AlarmData

    private lateinit var mSensorManager: SensorManager
    private lateinit var mAccelerometer: Sensor
    private var mAccel:Float = 0.toFloat()
    private var mAccelCurrent:Float = 0.toFloat()
    private var mAccelLast:Float = 0.toFloat()
    private var mShakeCount:Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }
        setContentView(R.layout.activity_shake)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        data = this.intent!!.getParcelableExtra("data")
        shakeText = findViewById(R.id.shakePercentage)

        snooze.setOnClickListener{
            if (snoozeAlarm(data, applicationContext)) {
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI, Handler())
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
                Toast.makeText(this, "Task solved! Alarm dismissed!", Toast.LENGTH_LONG).show()
                stopAlarm(data, applicationContext)
                finish()
                mSensorManager.unregisterListener(this, mAccelerometer)
            }
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var shakeText: TextView? = null
    }

}
