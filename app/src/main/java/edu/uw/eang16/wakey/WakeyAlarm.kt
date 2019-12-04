package edu.uw.eang16.wakey

import android.content.Context
import android.content.Intent

interface WakeyAlarm {
    // Uses the information from AlarmData to set off the alarm. Should be a part of onCreate.
    fun startAlarm(alarmData: AlarmData, context: Context) {
        context.startService(Intent(context.applicationContext, WakeySoundService::class.java).apply {
            putExtra("data", alarmData)
        })
    }

    // Upon successfully completing the task, stops the alarm
    fun stopAlarm(alarmData: AlarmData, context: Context) {
        context.stopService(Intent(context.applicationContext, WakeySoundService::class.java).apply {
            putExtra("data", alarmData)
        })
    }
}