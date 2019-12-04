package edu.uw.eang16.wakey

import android.net.Uri
import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class AlarmData(var id: String, var day: BooleanArray, var time: Calendar, var task: Task,
                     var ringtone: Uri, var volume: Int, var vibration: Int, var snooze: Int,
                     var limit: Int, var active: Boolean): Parcelable, Comparable<AlarmData> {
    override fun toString(): String {
        val date = SimpleDateFormat("HH:mm:ss").format(time.time)
        val stringy = id + "|" + day.joinToString(".") + "|" + date + "|" + task.name +
                "|" + ringtone.toString() + "|" + volume.toString() + "|" + vibration.toString() +
                "|" + snooze.toString() + "|" + limit.toString() + "|" + active.toString()
        return stringy
    }

    override fun compareTo(other: AlarmData): Int {
        return time.compareTo(other.time)
    }
}

enum class Task { NONE, MATH, SCAN, SHAKE, GAME, RANDOM }

val DAYS = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
val SNOOZE_TIMES = arrayOf(0, 1, 3, 5, 10, 15, 20, 30)

fun parseAlarmData(line: String): AlarmData {
    val sec = line.split("|")
    var id = sec[0].toString()
    var day = BooleanArray(7)
    for ((x, i) in sec[1].split(".").withIndex()) {
        day[x] = i.toBoolean()
    }
    var time = Calendar.getInstance()
    time.time = SimpleDateFormat("HH:mm:ss").parse(sec[2])
    var task = Task.valueOf(sec[3])
    var ringtone = Uri.parse(sec[4])
    var volume = sec[5].toInt()
    var vibration = sec[6].toInt()
    var snooze = sec[7].toInt()
    var limit = sec[8].toInt()
    var active = sec[9].toBoolean()
    return AlarmData(id, day, time, task, ringtone, volume, vibration, snooze, limit, active)
}