package edu.uw.eang16.wakey

import android.content.Context
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileReader


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (!filesDir.exists()) {
            filesDir.mkdirs()
        }
        val file = File(filesDir, "alarms")
        val rawText = FileReader(file).readText()

        fab.setOnClickListener { view ->
            val intent = Intent(this, EditAlarm::class.java)
            startActivity(intent)
        }
    }
}

enum class Task { Shake, Scanner, Math, None }

// Todo: Read all information unique to a single alarm
data class AlarmData(var id: Int, var day: String, var time: String, var task: Task, var ringtone: String,
                     var volume: Int, var vibration: Int, var snooze: Int, var limit: Int)

class ForecastAdapter(context: Context, objects: List<AlarmData>):
    ArrayAdapter<AlarmData>(context, R.layout.alarm_list, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val alarm = getItem(position)!!
        var holder: RecyclerView.ViewHolder

        if (convertView == null) {

        }
    }
}