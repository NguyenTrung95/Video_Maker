package com.devchie.videomaker.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import com.devchie.videomaker.R
import com.devchie.videomaker.ads.AdmobAds
import com.devchie.videomaker.ads.AdmobAds.OnAdsCloseListener
import com.devchie.videomaker.ads.FacebookAds
import com.devchie.videomaker.dialog.RateDialog
import com.devchie.videomaker.dialog.SettingDialog
import com.devchie.videomaker.model.MySharedPreferences
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class MainActivity : BaseSplitActivity() {
    var doubleBackToExitPressedOnce = false
    private var mLastClickTime: Long = 0
    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        if (!AdmobAds.showFullAdStart(null as OnAdsCloseListener?)) {
            FacebookAds.showFullAds(null)
        }
        setContentView(R.layout.activity_main)
        AdmobAds.loadBanner(this)
        AdmobAds.initFullAds(this)
        AdmobAds.loadNativeAds(this, null as View?)
        FacebookAds.initFullAds(this)
        FacebookAds.loadNativeAd(this)
        Thread.setDefaultUncaughtExceptionHandler { thread, th -> Log.e("EXCEPTION_IN_THREAD", thread.name + " : " + th.message) }
        addControls()


    }

    private fun addControls() {
        findViewById<View>(R.id.bt_new_project).setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime >= 1000) {
                mLastClickTime = SystemClock.elapsedRealtime()
                Dexter.withContext(this).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                           this@MainActivity.startActivity(Intent(this@MainActivity, SelectedPhotoActivity::class.java))
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied) {
                            SettingDialog.showSettingDialog(this@MainActivity)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(list: List<PermissionRequest>, permissionToken: PermissionToken) {
                        permissionToken.continuePermissionRequest()
                    }
                }).withErrorListener { Toast.makeText(this@MainActivity.applicationContext, "Error occurred! ", Toast.LENGTH_SHORT).show() }.onSameThread().check()
            }
        }
        findViewById<View>(R.id.bt_rate).setOnClickListener {
            rateApp(false)
        }
        findViewById<View>(R.id.bt_share).setOnClickListener {
            try {
                val intent = Intent("android.intent.action.SEND")
                intent.type = "text/plain"
                intent.putExtra("android.intent.extra.SUBJECT", "My application name")
                intent.putExtra("android.intent.extra.TEXT", """
     
     Let me recommend you this application
     
     "https://play.google.com/store/apps/details?id=$packageName
     """.trimIndent())
                startActivity(Intent.createChooser(intent, "Choose one"))
            } catch (unused: Exception) {
                Log.e("log",unused.message?:"")
            }

        }
        findViewById<View>(R.id.bt_policy).setOnClickListener {
            startActivity(Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.policy_url))))
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        Runtime.getRuntime().gc()
    }

    override fun onBackPressed() {
        if (!MySharedPreferences.getInstance(this).getBoolean("rated", false)) {
            rateApp(true)
        } else if (doubleBackToExitPressedOnce) {
            showGoodBye()
        } else {
            doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
    }

    private fun rateApp(z: Boolean) {
        RateDialog(this, z).show()
    }

    fun showGoodBye() {
        val onAdsCloseListener = OnAdsCloseListener {
            this@MainActivity.startActivity(Intent(this@MainActivity, ThankYouActivity::class.java))
            finish()
        }
        if (!AdmobAds.showFullAds(onAdsCloseListener)) {
            onAdsCloseListener.onAdsClose()
        }
    }
}