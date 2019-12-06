package edu.uw.eang16.wakey

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.io.File
import java.io.FileReader
import java.text.SimpleDateFormat
import java.util.*

class AlarmService : Service() {
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
            for ((index, day) in DAYS.withIndex()) {
                if (data.day[index] && curDay == day) {
                    soundAlarm(applicationContext, data)
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }


    fun soundAlarm(context: Context, data: AlarmData) {
        var task = data.task
        if (task == Task.RANDOM) {
            val rand = Random().nextInt(4) + 1
            task = Task.values()[rand]
        }
        val intent =
            when(task) {
                Task.NONE -> Intent(context, NoTask::class.java)
                Task.MATH -> Intent(context, MathSolver::class.java)
                Task.SCAN -> Intent(context, ScanActivity::class.java)
                Task.SHAKE -> Intent(context, ShakeActivity::class.java)
                Task.GAME -> Intent(context, PuzzleActivity::class.java)
                else -> Intent(context, NoTask::class.java)
            }
        intent.putExtra("data", data)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}