package edu.uw.eang16.wakey

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_scan_code.*

class ScanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_code)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        scanBtn.setOnClickListener{
            val intent = Intent(this, Scanner::class.java)
            startActivity(intent)
        }
    }

}
