package edu.uw.eang16.wakey

import android.content.Context
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.graphics.Color
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Switch
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import java.io.FileReader
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var aList: MutableList<AlarmData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        
        aList = mutableListOf()

        fab.setOnClickListener { view ->
            val intent = Intent(this, EditAlarm::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        updateList()
    }

    fun updateList() {
        aList.clear()
        if (!filesDir.exists()) {
            filesDir.mkdirs()
        }
        val file = File(filesDir, "alarms")
        if (!file.exists()) {
            file.createNewFile()
        }
        val rawText = FileReader(file).readText()
        val rawList = rawText.split("\n")
        for (i in rawList) {
            aList.add(parseAlarmData(i))
        }
        val adapter = AlarmAdapter(applicationContext, aList)
        alarmList.adapter = adapter
    }
}

private class ViewHolder(var time: TextView? = null, var active: Switch? = null, var days: ConstraintLayout? = null)

class AlarmAdapter(context: Context, objects: List<AlarmData>):
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
        holder.time!!.text = SimpleDateFormat("hh:mm a").format(alarm.time.time)
        //Todo: set the day color to be proper
        holder.active!!.isChecked = alarm.active
        holder.time!!.tag = alarm
        for (i in 0 until holder.days!!.childCount) {
            val tv = holder.days!!.getChildAt(i) as TextView
            if (alarm.day[i]) {
                tv.setTextColor(Color.rgb(0,191,255))
            }
        }

        view!!.setOnClickListener {
            val data = it.findViewById<View>(R.id.alarmTime).tag as AlarmData
            val intent = Intent(context, EditAlarm::class.java).putExtra("data", data)
            context.startActivity(intent)
        }

        return view!!
    }
}