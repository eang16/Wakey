package edu.uw.eang16.wakey

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView


class Scanner : AppCompatActivity(), ZXingScannerView.ResultHandler{
    private var mScannerView: ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
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
        /*val sharedPref= getSharedPreferences("scanner", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("result", rawResult.text)
        editor.apply()*/
        ScanActivity.scanText!!.text = "Result: ${rawResult.text}"
        onBackPressed()
    }
}