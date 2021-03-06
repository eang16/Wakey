package edu.uw.eang16.wakey

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.*

interface WakeyAlarm {
    // Uses the information from AlarmData to set off the alarm. Should be a part of onCreate.
    fun startAlarm(alarmData: AlarmData, context: Context) {
        val intent = Intent(context, WakeySoundService::class.java)
        intent.putExtra("data", alarmData)
        context.applicationContext.startService(intent)
    }

    // Upon successfully completing the task, stops the alarm
    fun stopAlarm(alarmData: AlarmData, context: Context) {
        context.applicationContext.stopService(Intent(context.applicationContext, WakeySoundService::class.java).apply {
            putExtra("data", alarmData)
        })
    }

    // Snoozes the alarm by unsetting it, decrementing the snooze counter, and then continuing
    fun snoozeAlarm(alarmData: AlarmData, context: Context): Boolean {
        if (alarmData.limit > 0) {
            stopAlarm(alarmData, context.applicationContext)

            AlarmHelper.getAlarmManager(context.applicationContext).setExact(
                AlarmManager.RTC_WAKEUP,
                Calendar.getInstance().timeInMillis + SNOOZES[alarmData.snooze] * 60 * 1000,
                AlarmHelper.getIntent(alarmData, context.applicationContext)
            )

            return true
        } else {
            return false
        }
    }
}