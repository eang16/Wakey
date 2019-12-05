package edu.uw.eang16.wakey

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.code_list.*
import kotlinx.android.synthetic.main.save_code.*
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class QRBarcodeList : AppCompatActivity() {
    private val codeList = arrayListOf<String>()

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

        listView.setOnItemClickListener { _, _, i, _ ->
            val item = listView.adapter.getItem(i).toString()
            selectedCode = readResult(item)
            updatePosition(i.toString(), i.toString())
            selectedPosition = readPosition(i.toString())
            deleteCode.setOnClickListener{
                deleteCode(item)
            }
        }

        if (selectedPosition != null) {
            listView.setItemChecked(selectedPosition!!.toInt(), true)
        }
    }

    private fun readResult(fileName: String):String {
        val myFile = File(filesDir, fileName)
        return FileReader(myFile).readText()
    }

    private fun readPosition(fileName: String): String {
        val myFile = File(filesDir, fileName)
        return FileReader(myFile).readText()
    }

    private fun updatePosition(fileName: String, value: String) {
        val myFile = File(filesDir, fileName)
        val writer = FileWriter(myFile, false)
        writer.write(value)
        writer.close()
    }

    private fun deleteCode(fileName: String) {
        File(filesDir, fileName).delete()
    }

    companion object {
        var selectedCode: String? = null
        var selectedPosition: String? = null
    }

}
