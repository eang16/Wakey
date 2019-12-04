package edu.uw.eang16.wakey

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.util.Log

class AlarmHelper {
    companion object {
        private var alarmManager: AlarmManager? = null

        fun getAlarmManager(context: Context): AlarmManager {
            if (alarmManager == null) {
                alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
            }
            return alarmManager!!
        }

        fun getIntent(data: AlarmData, context: Context): PendingIntent {
            var intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("id", data.id)
            }
            return PendingIntent.getService(context, data.id.toInt(), intent, 0)
        }
    }
}

fun activateAlarm(data: AlarmData, context: Context) {
    val am = AlarmHelper.getAlarmManager(context)
    deactivateAlarm(data, context)
    am.setInexactRepeating(
        AlarmManager.RTC_WAKEUP,
        data.time.timeInMillis,
        AlarmManager.INTERVAL_DAY,
        AlarmHelper.getIntent(data, context)
    )

    Log.e("msg", "Alarm set: " + data.id)
}

fun deactivateAlarm(data: AlarmData, context: Context) {
    val am = AlarmHelper.getAlarmManager(context)
    am.cancel(AlarmHelper.getIntent(data, context))
    Log.e("msg", "Alarm Canceled: " + data.id)
}