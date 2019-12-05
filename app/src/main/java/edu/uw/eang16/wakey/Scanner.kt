package edu.uw.eang16.wakey

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import android.content.Intent




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
            val intent = Intent(this, QRBarcodeList::class.java)
            codeArray.add(label)
            intent.putExtra(label, rawResult.text)
            //codeArray.add(rawResult.text)
            startActivity(intent)}
        builder.setNegativeButton("Cancel") { _, _ ->
            mScannerView!!.resumeCameraPreview(this)
        }
        val dialog = builder.show()
        dialog.setCanceledOnTouchOutside(false)

        /*
        mScannerView!!.resumeCameraPreview(this)
            dialog.dismiss()
         */

    }
    companion object {
        var codeArray = arrayListOf<String>()
    }

}