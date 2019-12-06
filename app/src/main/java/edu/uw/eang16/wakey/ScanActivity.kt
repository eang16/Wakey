package edu.uw.eang16.wakey

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_scan_code.*
import kotlinx.android.synthetic.main.activity_scan_code.snooze
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

        snooze.setOnClickListener{
            if (snoozeAlarm(data, this)) {
                finish()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()

    }

    override fun handleResult(rawResult: Result) {
        if (rawResult.text == QRBarcodeList.selectedCode.toString()) {
            Toast.makeText(this, "Task solved! Alarm dismissed!", Toast.LENGTH_SHORT).show()
            stopAlarm(data, applicationContext)
            finish()
        } else {
            Toast.makeText(this, "Wrong QR/Barcode. Scan again!", Toast.LENGTH_SHORT).show()
            mScannerView!!.resumeCameraPreview(this)

        }
    }
}
