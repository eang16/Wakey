package edu.uw.eang16.wakey

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.size
import com.google.zxing.Result
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

        val myFile = File(filesDir, "codeList")
        if (!myFile.exists()) {
            myFile.createNewFile()
        }


        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice,  codeList)

        val listView = findViewById<ListView>(R.id.listview)

        listView.adapter = adapter
        val codeArray = readCodeList()

        if (codeArray.isNotEmpty()) {
            codeList.addAll(codeArray)
            adapter.notifyDataSetChanged()
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

        }

        listView.setOnItemLongClickListener { parent, view, position, id ->
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete QR/Barcode")
            builder.setPositiveButton("Delete") { _, _ ->
                val item = listView.adapter.getItem(position).toString()
                deleteCode(item)
                adapter.notifyDataSetChanged()
            }
            val dialog = builder.show()
            dialog.setCanceledOnTouchOutside(true)
            true
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

    private fun readCodeList(): List<String> {
        val myFile = File(filesDir, "codeList")
        val codeArray = FileReader(myFile).readText().split("\n")
        var result = mutableListOf<String>()
        for (item in codeArray) {
            if (item.isNotBlank() && item.isNotEmpty()) {
                result.add(item)
            }
        }
        return result
    }

    private fun updatePosition(fileName: String, value: String) {
        val myFile = File(filesDir, fileName)
        if (!myFile.exists()) { myFile.createNewFile() }
        val writer = FileWriter(myFile, false)
        writer.write(value)
        writer.close()
    }

    private fun deleteCode(fileName: String) {
        File(filesDir, fileName).delete()
        codeList.remove(fileName)
        val myFile = File(filesDir, "codeList")
        val writer = FileWriter(myFile, false)
        for (item in codeList) {
            writer.write(item + "\n")
        }
        writer.close()
    }

    companion object {
        var selectedCode: String? = null
        var selectedPosition: String? = null
    }

}
