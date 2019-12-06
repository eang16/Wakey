package edu.uw.eang16.wakey

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_no_task.*
import java.text.SimpleDateFormat
import java.util.*

class NoTask : AppCompatActivity(), WakeyAlarm {
    lateinit var data: AlarmData

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
        setContentView(R.layout.activity_no_task)
        data = intent.getParcelableExtra("data")!!
        time.text = SimpleDateFormat("hh:mm a").format(Calendar.getInstance().time)

        snoozeBtn.setOnClickListener {
            if (snoozeAlarm(data, applicationContext)) {
                finish()
            }
        }

        stopBtn.setOnClickListener {
            stopAlarm(data, applicationContext)
            finish()
        }
    }
}

