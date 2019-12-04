package edu.uw.eang16.wakey

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class AlarmData(var id: Int, var day: BooleanArray, var time: Calendar, var task: Task,
                     var ringtone: Uri, var volume: Int, var vibration: Int, var snooze: Int,
                     var limit: Int, var active: Boolean): Parcelable

enum class Task { NONE, MATH, SCAN, SHAKE, GAME, RANDOM }