package edu.uw.eang16.wakey

import android.content.Context
import android.content.Intent

// Remember to call superfunctions on all the below!
interface WakeyAlarm {
    // Uses the information from AlarmData to set off the alarm. Should be a part of onCreate.
    fun startAlarm(alarmData: AlarmData, context: Context) {
        context.startService(Intent(context.applicationContext, WakeySoundService::class.java).apply {
            putExtra("data", alarmData)
        })
    }

    // When the alarm wakes up after a snooze, this resumes the ringtone and alarm
    fun resumeAlarm(alarmData: AlarmData, context: Context) {
        context.startService(Intent(context.applicationContext, WakeySoundService::class.java).apply {
            putExtra("data", alarmData)
        })
    }

    // Keeps track of how many snoozes are available and disables snooze if none are left
    fun snoozeAlarm(alarmData: AlarmData, context: Context) {
        context.stopService(Intent(context.applicationContext, WakeySoundService::class.java).apply {
            putExtra("data", alarmData)
        })
        val snoozeTimeInMinutes = SNOOZE_TIMES[alarmData.snooze]
    }

    // Upon successfully completing the task, stops the alarm
    fun stopAlarm(alarmData: AlarmData, context: Context) {
        context.stopService(Intent(context.applicationContext, WakeySoundService::class.java).apply {
            putExtra("data", alarmData)
        })
    }
}