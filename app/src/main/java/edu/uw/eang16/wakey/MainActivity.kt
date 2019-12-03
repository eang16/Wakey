package edu.uw.eang16.wakey

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
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

    // Todo: Read all information unique to a single alarm
    data class AlarmInfo(var name: String, var day: Char, var time: String)
}
