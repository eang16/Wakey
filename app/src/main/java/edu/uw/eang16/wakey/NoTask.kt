package edu.uw.eang16.wakey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import kotlinx.android.synthetic.main.activity_no_task.*
import java.text.SimpleDateFormat
import java.util.*

class NoTask : AppCompatActivity(), WakeyAlarm {
    lateinit var data: AlarmData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_task)
        data = intent.getParcelableExtra("data")!!
        startAlarm(data, this)
        time.text = SimpleDateFormat("hh:mm a").format(data.time.time)

        snoozeBtn.setOnClickListener {
            if (snoozeAlarm(data, it.context)) {
                Log.e("msg", "Snooze button pressed")
                finish()
            }
        }

        stopBtn.setOnClickListener {
            stopAlarm(data, it.context)
            finish()
        }
    }
}
