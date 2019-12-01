package edu.uw.eang16.wakey

import android.app.Activity
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_edit_alarm.*
import kotlinx.android.synthetic.main.alarm_list.*
import java.text.SimpleDateFormat
import java.util.*
import android.view.Menu
import android.view.MenuItem
import android.media.RingtoneManager.TYPE_ALARM

import android.net.Uri

import android.content.Intent

class EditAlarm : AppCompatActivity(), AdapterView.OnItemSelectedListener  {
    var tasks = arrayOf("None", "Solve math problem", "Scan QR/Barcode", "Shake your phone",
            "Play a game", "Random")
    private lateinit var ringTone: Ringtone
    private lateinit var uriAlarm: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_alarm)

        val cal = Calendar.getInstance()
        time.text = SimpleDateFormat("hh:mm a").format(cal.time)

        //task spinner dropdown
        taskSpinner.onItemSelectedListener = this
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tasks)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        taskSpinner!!.adapter = arrayAdapter

        //time picker
        time.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                time.text = SimpleDateFormat("hh:mm a").format(cal.time)
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()

        }

        // ringtone picker
        uriAlarm = RingtoneManager.getDefaultUri(TYPE_ALARM);
        ringtone.setOnClickListener {
            ringTone = RingtoneManager.getRingtone(applicationContext, uriAlarm)
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            startActivityForResult(intent, 1)

            fun onClick(v: View) {
                if (ringTone != null) {
                    ringTone.stop();
                }
                ringTone.play()
            }
        }

        //test shaker
        shakerbtn.setOnClickListener {
            val intent = Intent(this, ShakeServiceActivity::class.java)
            startActivity(intent)
        }
    }

    // for task
    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
    }
    // for task
    override fun onNothingSelected(arg0: AdapterView<*>) {
    }

    // for ringtone
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val uri = data!!.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)

            if (uri != null) {
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
            R.id.action_save -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    // for snooze duration
    fun snoozeDurationDialog(v: View) {
        val snoozeDurationItems = arrayOf("None", "1 minute", "3 minutes", "5 minutes", "10 minutes", "15 minutes", "20 minutes", "30 minutes")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Snooze Duration")
        val checkedItem = 0
        builder.setSingleChoiceItems(snoozeDurationItems, checkedItem) { _, which ->
            snoozeDuration.text = snoozeDurationItems[which]
        }
        builder.setPositiveButton("Done") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    // for snooze limit
    fun snoozeLimitDialog(v: View) {
        val snoozeLimitItems = arrayOf("None", "1 time", "2 times", "3 times", "4 times", "5 times")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Snooze Limit")
        val checkedItem = 0
        builder.setSingleChoiceItems(snoozeLimitItems, checkedItem) { _, which ->
            snoozeLimit.text = snoozeLimitItems[which]
        }
        builder.setPositiveButton("Done") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
}
