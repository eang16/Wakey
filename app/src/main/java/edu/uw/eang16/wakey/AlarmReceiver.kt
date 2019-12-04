package edu.uw.eang16.wakey

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.io.File
import java.io.FileReader
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : Service() {
    private lateinit var data: AlarmData

    override fun onBind(intent: Intent?): IBinder? { return null }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val id = intent.getStringExtra("id")
        if (id != null) {
            if (!applicationContext.filesDir.exists()) { applicationContext.filesDir.mkdirs() }
            val file = File(applicationContext.filesDir, "alarms")
            if (!file.exists()) { file.createNewFile() }
            val alarmsText = FileReader(file).readLines()
            for (line in alarmsText) {
                if (line.startsWith(id)) { data = parseAlarmData(line) }
            }

            val curDay = SimpleDateFormat("EEEE", Locale.ENGLISH).format(Calendar.getInstance().time)
            for ((index, day) in Days.withIndex()) {
                if (data.day[index] && curDay == day) {
                    soundAlarm(applicationContext, data)
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }


    fun soundAlarm(context: Context, data: AlarmData) {
        Log.e("msg", data.id + " Sounding")
    }
}