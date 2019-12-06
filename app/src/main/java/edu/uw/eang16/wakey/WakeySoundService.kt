package edu.uw.eang16.wakey

import android.app.*
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
    private val CHANNEL_ID = "Wakey Alarm Clock Notification Channel"
    private var notifChannel: NotificationChannel? = null
    private var notifManager: NotificationManager? = null
    private var player: MediaPlayer? = null

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

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(context).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(intent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }


        var builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Wakey")
            .setContentIntent(pendingIntent)
            .setContentText("Currently sounding an alarm!")
            .setAutoCancel(true)
            .build()

        return builder
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        data = intent!!.getParcelableExtra("data") as AlarmData
        player = MediaPlayer.create(this, data.ringtone).apply {
            isLooping = true
            setVolume(data.volume.toFloat() / 100, data.volume.toFloat() / 100)
            start()
        }
        startForeground(data.id.toInt(), buildNotification(this))
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        stopForeground(true)
        player?.stop()
        super.onDestroy()
    }
}