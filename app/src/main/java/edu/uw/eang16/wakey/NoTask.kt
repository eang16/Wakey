package edu.uw.eang16.wakey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_no_task.*

class NoTask : AppCompatActivity(), WakeyAlarm {
    lateinit var data: AlarmData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_task)
        data = intent.getParcelableExtra("data")!!

        snoozeBtn.setOnClickListener {
            if (snoozeAlarm(data, it.context)) {
                finish()
            }
        }

        stopBtn.setOnClickListener {
            stopAlarm(data, it.context)
            finish()
        }
    }
}
