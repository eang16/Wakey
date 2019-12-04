package edu.uw.eang16.wakey

interface WakeyAlarm {
    // Uses the information from AlarmData to set off the alarm. Should be a part of onCreate.
    fun startAlarm(alarmData: AlarmData)

    // When the alarm wakes up after a snooze, this resumes the ringtone and alarm
    fun resumeAlarm(alarmData: AlarmData)

    // Keeps track of how many snoozes are available and disables snooze if none are left
    fun snoozeAlarm(alarmData: AlarmData) {
        val snoozeTimeInMinutes = SNOOZE_TIMES[alarmData.snooze]
    }

    // Upon successfully completing the task, stops the alarm
    fun stopAlarm()
}