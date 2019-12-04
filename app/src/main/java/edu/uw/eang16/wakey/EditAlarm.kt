package edu.uw.eang16.wakey

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_edit_alarm.*
import kotlinx.android.synthetic.main.alarm_list.*
import java.text.SimpleDateFormat
import java.util.*
import android.view.Menu
import android.view.MenuItem
import android.media.RingtoneManager.TYPE_ALARM

import android.net.Uri

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.ColorSpace
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class EditAlarm : AppCompatActivity(), AdapterView.OnItemSelectedListener  {
    var tasks = arrayOf("None", "Solve math problem", "Scan QR/Barcode", "Shake your phone",
            "Play a game", "Random")
    val snoozeDurationItems = arrayOf("None", "1 minute", "3 minutes", "5 minutes", "10 minutes", "15 minutes", "20 minutes", "30 minutes")
    val snoozeLimitItems = arrayOf("None", "1 time", "2 times", "3 times", "4 times", "5 times")
    var taskMap = hashMapOf<Task, Int>()

    private lateinit var ringTone: Ringtone
    private lateinit var uriAlarm: Uri
    private lateinit var data: AlarmData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_alarm)
        taskMap[Task.NONE] = 0
        taskMap[Task.MATH] = 1
        taskMap[Task.SCAN] = 2
        taskMap[Task.SHAKE] = 3
        taskMap[Task.GAME] = 4
        taskMap[Task.RANDOM] = 5

        if (intent.hasExtra("data")) {
            data = intent.extras!!["data"] as AlarmData
        } else {
            var id = Random().nextInt().toString()
            var day = booleanArrayOf(false, false, false, false, false, false, false)
            var dtime = Calendar.getInstance().apply {
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            var task = Task.NONE
            var ringtone = RingtoneManager.getActualDefaultRingtoneUri(applicationContext, RingtoneManager.TYPE_RINGTONE)
            var volume = 100
            var vibration = 100
            var snooze = 3
            var limit = 3
            var active = true
            data = AlarmData(id, day, dtime, task, ringtone, volume, vibration, snooze, limit, active)
        }
        time.text = SimpleDateFormat("hh:mm a").format(data.time.time)

        //Sliders
        volumeBar.progress = data.volume
        vibrationBar.progress = data.vibration
        volumeBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                data.volume = i
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        vibrationBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                data.vibration = i
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        //task spinner dropdown
        taskSpinner.onItemSelectedListener = this
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tasks)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        taskSpinner!!.adapter = arrayAdapter
        taskSpinner.setSelection(taskMap[data.task]!!)

        //time picker
        time.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                data.time.set(Calendar.HOUR_OF_DAY, hour)
                data.time.set(Calendar.MINUTE, minute)
                time.text = SimpleDateFormat("hh:mm a").format(data.time.time)
            }
            TimePickerDialog(this, timeSetListener, data.time.get(Calendar.HOUR_OF_DAY), data.time.get(Calendar.MINUTE), false).show()
        }

        // ringtone picker
        ringtone.text = RingtoneManager.getRingtone(applicationContext, data.ringtone).getTitle(this)
        uriAlarm = data.ringtone
        ringtone.setOnClickListener {
            ringTone = RingtoneManager.getRingtone(applicationContext, uriAlarm)
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            startActivityForResult(intent, 1)

            fun onClick(v: View) {
                if (ringTone != null) {
                    ringTone.stop()
                }
                ringTone.play()
            }
        }

        // Day buttons
        for (i in 0 until buttonWrapper.childCount) {
            val btn = buttonWrapper.getChildAt(i) as ToggleButton
            btn.isChecked = data.day[i]
            if (btn.isChecked) {
                btn.setBackgroundResource(R.drawable.circle_selected)
            } else {
                btn.setBackgroundResource(R.drawable.circle)
            }
            btn.setOnCheckedChangeListener { buttonView, isChecked ->
                data.day[(btn.tag as String).toInt()] = isChecked
                if (isChecked) {
                    btn.setBackgroundResource(R.drawable.circle_selected)
                } else {
                    btn.setBackgroundResource(R.drawable.circle)
                }
            }
        }

        // Snooze duration and limit
        snoozeDuration.text = snoozeDurationItems[data.snooze]
        snoozeLimit.text = snoozeLimitItems[data.limit]

        //test shaker
        shakerbtn.setOnClickListener {
            val intent = Intent(this, ShakeServiceActivity::class.java)
            startActivity(intent)
        }

        //test scanner
        scannerbtn.setOnClickListener {
            val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
                val intent = Intent(this, ScanActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, ScanActivity::class.java)
                startActivity(intent)
            }
        }

        //test Puzzle
        puzzlebtn.setOnClickListener {
            val intent = Intent(this, PuzzleActivity::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
        }

        //test Math
        mathbtn.setOnClickListener {
            val intent = Intent(this, MathSolver::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
        }
    }

    // for task
    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        for (i in taskMap.keys) {
            if (taskMap[i] == position) {
                data.task = i
            }
        }
    }
    // for task
    override fun onNothingSelected(arg0: AdapterView<*>) {
    }

    // for ringtone
    override fun onActivityResult(requestCode: Int, resultCode: Int, ddata: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val uri = ddata!!.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)

            if (uri != null) {
                data.ringtone = uri
                ringtone.text = RingtoneManager.getRingtone(applicationContext, uri).getTitle(this)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_save -> saveAlarm()
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun saveAlarm(): Boolean {
        if (!filesDir.exists()) { filesDir.mkdirs() }
        val file = File(filesDir, "alarms")
        if (!file.exists()) { file.createNewFile() }

        val alarmsText = FileReader(file).readLines()
        var index = -1
        for ((i, line) in alarmsText.withIndex()) {
            if (line.startsWith(data.id)) { index = i }
        }

        // Index is -1 if the alarm already exists, and this is an update
        val stringArray = alarmsText.toMutableList()
        val stringRepresentation = data.toString()
        if (index == -1) {
            stringArray.add(stringRepresentation)
        } else {
            stringArray[index] = stringRepresentation
        }
        FileWriter(file, false).apply {
            write(stringArray.joinToString("\n"))
            close()
        }
        activateAlarm(data, applicationContext)
        return true
    }

    // for snooze duration
    fun snoozeDurationDialog(v: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Snooze Duration")
        val checkedItem = data.snooze
        builder.setSingleChoiceItems(snoozeDurationItems, checkedItem) { _, which ->
            snoozeDuration.text = snoozeDurationItems[which]
            data.snooze = snoozeDurationItems.indexOf(snoozeDuration.text)
        }
        builder.setPositiveButton("Done") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    // for snooze limit
    fun snoozeLimitDialog(v: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Snooze Limit")
        val checkedItem = data.limit
        builder.setSingleChoiceItems(snoozeLimitItems, checkedItem) { _, which ->
            snoozeLimit.text = snoozeLimitItems[which]
            data.limit = snoozeLimitItems.indexOf(snoozeLimit.text)
        }
        builder.setPositiveButton("Done") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
}
