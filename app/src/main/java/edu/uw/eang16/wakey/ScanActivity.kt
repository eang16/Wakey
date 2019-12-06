package edu.uw.eang16.wakey

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_scan_code.*
import kotlinx.android.synthetic.main.activity_scan_code.snooze
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScanActivity : AppCompatActivity(), WakeyAlarm, ZXingScannerView.ResultHandler {
    lateinit var data: AlarmData

    private var mScannerView: ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }
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
            if (snoozeAlarm(data, applicationContext)) {
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
