package edu.uw.eang16.wakey

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.code_list.*
import java.io.File
import java.io.FileReader

class QRBarcodeList : AppCompatActivity() {
    private val codeList = arrayListOf<String>()
    private var selectedPosition: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.code_list)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, codeList)

        val listView = findViewById<ListView>(R.id.listview)
        listView.adapter = adapter

        codeList.addAll(Scanner.codeArray)
        adapter.notifyDataSetChanged()

        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        }

        scan.setOnClickListener {
            val intent = Intent(this, Scanner::class.java)
            startActivity(intent)
        }

        // get selected barcode reference

        listView.setOnItemClickListener { _, _, i, _ ->
            val item = listView.adapter.getItem(i).toString()
            selectedCode = read(item)
            //selectedPosition = i
        }

        // remember the selected choice
        //listView.setItemChecked(selectedPosition, true)
    }

    /*override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }*/

    companion object {
        var selectedCode: String? = null
    }

    fun read(fileName: String):String {
        val myFile = File(filesDir, fileName)
        return FileReader(myFile).readText()
    }


}
