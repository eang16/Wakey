package edu.uw.eang16.wakey

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class WakeySoundService: Service() {
    lateinit var data: AlarmData
    lateinit var ringtone: Ringtone

    companion object {
        private val CHANNEL_ID = "Wakey Alarm Clock Notification Channel"
        private var mediaPlayer: MediaPlayer? = null
        private var notifChannel: NotificationChannel? = null
        private var notifManager: NotificationManager? = null

        private fun getNC(): NotificationChannel? {
            if (notifChannel == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = CHANNEL_ID
                val descriptionText = "The Wakey App"
                val importance = NotificationManager.IMPORTANCE_HIGH
                notifChannel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
            }
            return notifChannel
        }

        private fun getNM(context: Context): NotificationManager {
            if (notifManager == null) {
                notifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return notifManager!!
        }

        private fun buildNotification(context: Context): Notification {
            val channel = getNC()
            val manager = getNM(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                manager.createNotificationChannel(channel!!)
            }

            //TODO: Make an intent which returns to the currently playing alarm (singleTop?)
            val intent = Intent(context, MainActivity::class.java)


            var builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Wakey")
                .setContentText("Currently sounding an alarm!")
                .setAutoCancel(true)
                .build()

            return builder
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("msg", "Music should be playing right about now")
        data = intent!!.getParcelableExtra("data") as AlarmData
        startForeground(data.id.toInt(), buildNotification(applicationContext))
        ringtone = RingtoneManager.getRingtone(applicationContext, data.ringtone)
        ringtone.play()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        stopForeground(true)
        if (ringtone != null) {
            ringtone.stop()
        }
        super.onDestroy()
    }
}