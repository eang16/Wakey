package edu.uw.eang16.wakey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import android.content.Intent
import java.io.File
import java.io.FileWriter


class Scanner : AppCompatActivity(), ZXingScannerView.ResultHandler{
    private var mScannerView: ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        super.onCreate(savedInstanceState)
        mScannerView = ZXingScannerView(this)
        setContentView(mScannerView)
    }

    override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()

    }

    override fun handleResult(rawResult: Result) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("Save QR/Barcode")
        val dialogLayout = inflater.inflate(R.layout.save_code, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.editTxt)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Done") { _, _ ->
            val label = editText.text.toString()
            updateResult(label, rawResult.text)
            val intent = Intent(this, QRBarcodeList::class.java)
            updateCodeList(label)
            startActivity(intent)}
        builder.setNegativeButton("Cancel") { _, _ ->
            mScannerView!!.resumeCameraPreview(this)
        }
        val dialog = builder.show()
        dialog.setCanceledOnTouchOutside(false)
    }

    private fun updateResult(fileName: String, value: String) {
        val myFile = File(filesDir, fileName)
        if (!myFile.exists()) { myFile.createNewFile() }
        val writer = FileWriter(myFile, false)
        writer.write(value)
        writer.close()
    }

    private fun updateCodeList(label: String) {
        val myFile = File(filesDir, "codeList")
        val writer = FileWriter(myFile, true)
        writer.write(label + "\n")
        writer.close()
    }
}