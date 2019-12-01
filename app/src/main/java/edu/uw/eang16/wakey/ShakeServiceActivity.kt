package edu.uw.eang16.wakey

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.TextView

class ShakeServiceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shake);
        val intent = Intent(this, ShakeService::class.java)
        startService(intent)
        shakeText = findViewById(R.id.shakePercentage)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var shakeText: TextView
    }

}
