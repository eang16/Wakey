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
            var intent = Intent(context, AlarmService::class.java).apply {
                putExtra("id", data.id)
            }
            return PendingIntent.getService(context, data.id.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }
}

fun activateAlarm(data: AlarmData, context: Context) {
    val am = AlarmHelper.getAlarmManager(context)
    deactivateAlarm(data, context)
    Log.e("msg", data.toString())
    Log.e("msg", data.time.toString() + "|" + data.time.timeInMillis)
    am.setRepeating(
        AlarmManager.RTC_WAKEUP,
        data.time.timeInMillis,
        86400000,
        AlarmHelper.getIntent(data, context)
    )

    Log.e("msg", "Alarm set: " + data.id)
}

fun deactivateAlarm(data: AlarmData, context: Context) {
    val am = AlarmHelper.getAlarmManager(context)
    val pd = AlarmHelper.getIntent(data, context)
    pd.cancel()
    am.cancel(pd)
    Log.e("msg", "Alarm Canceled: " + data.id)
}