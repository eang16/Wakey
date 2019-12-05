package edu.uw.eang16.wakey

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_shake.*

class ShakeActivity : AppCompatActivity(), WakeyAlarm {
    lateinit var data: AlarmData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shake)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        data = intent.getParcelableExtra("data")

        val intent = Intent(this, ShakeService::class.java)
        startService(intent)

        startAlarm(data, this)
        shakeText = findViewById(R.id.shakePercentage)

        if (shakeText!!.text == "100%") {
            stopAlarm(data, this)
            Toast.makeText(this, "Task solved! Alarm dismissed!", Toast.LENGTH_SHORT).show()
            finish()
        }

        snooze.setOnClickListener{
            if (snoozeAlarm(data, this)) {
                finish()
            }
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var shakeText: TextView? = null
    }

}
