package edu.uw.eang16.wakey

import android.content.Context
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Switch
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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

private class ViewHolder(var time: TextView? = null, var active: Switch? = null, var days: ConstraintLayout? = null)

enum class Task { Shake, Scanner, Math, None }

// Todo: Read all information unique to a single alarm
data class AlarmData(var id: Int, var day: String, var time: String, var task: Task, var ringtone: String,
                     var volume: Int, var vibration: Int, var snooze: Int, var limit: Int, var active: Boolean)

class ForecastAdapter(context: Context, objects: List<AlarmData>):
    ArrayAdapter<AlarmData>(context, R.layout.alarm_list, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val alarm = getItem(position)!!
        var holder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.alarm_list, parent, false)
            holder = ViewHolder()
            holder.time = view.findViewById(R.id.alarmTime)
            holder.active = view.findViewById(R.id.switchBtn)
            holder.days = view.findViewById(R.id.days)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }
        holder.time!!.text = alarm.time
        //Todo: set the day color to be proper
        holder.active!!.isChecked = alarm.active
        holder.time!!.tag = alarm

        view!!.setOnClickListener {
            val thisAlarmData = it.findViewById<View>(R.id.alarmTime).tag as AlarmData
            // TODO: Start an activity for editing an alarm
        }

        return view!!
    }
}