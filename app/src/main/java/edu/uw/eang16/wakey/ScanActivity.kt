package edu.uw.eang16.wakey

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_scan_code.*
import kotlinx.android.synthetic.main.activity_scan_code.snooze
import kotlinx.android.synthetic.main.activity_shake.*
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScanActivity : AppCompatActivity(), WakeyAlarm, ZXingScannerView.ResultHandler {
    lateinit var data: AlarmData

    private var mScannerView: ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_code)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        scanBtn.setOnClickListener{
            mScannerView = ZXingScannerView(this)
            setContentView(mScannerView)
            mScannerView!!.setResultHandler(this)
            mScannerView!!.startCamera()
        }

        data = this.intent!!.getParcelableExtra("data")
        startAlarm(data, this)

        snooze.setOnClickListener{
            if (snoozeAlarm(data, this)) {
                finish()
            }
        }
    }
    /*
    override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()
    } */

    override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()

    }

    override fun handleResult(rawResult: Result) {
        if (rawResult.text == QRBarcodeList.selectedCode) {
            Toast.makeText(this, "Task solved! Alarm dismissed!", Toast.LENGTH_SHORT).show()
            stopAlarm(data, this)
            finish()
        } else {
            Toast.makeText(this, "Wrong QR/Barcode. Scan again!", Toast.LENGTH_SHORT).show()
            mScannerView!!.resumeCameraPreview(this)

        }
    }

}
