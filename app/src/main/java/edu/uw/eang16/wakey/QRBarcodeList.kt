package edu.uw.eang16.wakey

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.code_list.*

class QRBarcodeList : AppCompatActivity() {
    private val codeList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.code_list)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, codeList)

        val listView = findViewById<ListView>(R.id.listview)
        listView.adapter = adapter

        codeList.addAll(Scanner.codeArray)
        adapter.notifyDataSetChanged()

        scan.setOnClickListener {
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
    }

}
