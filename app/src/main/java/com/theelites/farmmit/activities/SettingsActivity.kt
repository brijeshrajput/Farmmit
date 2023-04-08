package com.theelites.farmmit.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.bunty.mietbradmin.Utils.Helper
import com.theelites.farmmit.R
import com.theelites.farmmit.databinding.ActivitySettingsBinding
import com.theelites.farmmit.util.AboutDialog
import com.theelites.farmmit.util.SharedPref
import java.lang.Exception

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    var sharedPref: SharedPref? = null
    var URL_TO_RESTORE: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        Helper.dark_mode(this)
        Helper.fullscreen_mode(this)
        Helper.screen_state(this)
        Helper.notification_sound(this)
        Helper.notification_vibration(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_settings)

        sharedPref = SharedPref(this)


        URL_TO_RESTORE = intent.getStringExtra("URL_TO_RESTORE")


        /*---------------- SwitchCompat ---------------*/
        val nightMode = binding.nightMode
        val screenOn = binding.keepScreen
        val screenMode = binding.fullScreen
        val notifySound = binding.notifySound
        val notifyVibration = binding.notifyVibration
        val downloadWifi = binding.downloadWifi

        /*---------------- Data as Links, Content ---------------*/
        val shareApp = binding.shareApp
        val sendFeedback = binding.sendFeedback
        val policyPrivacy = binding.privacyPolicy
        val policyTerms = binding.termsOfUse
        val aboutApp = binding.aboutApp


        /*---------------- Back Arrow, MainActivity ---------------*/
        val settingsBack = findViewById<ImageView>(R.id.go_back)

        settingsBack.setOnClickListener { view: View? ->
            super.onBackPressed()
        }

        /*---------------- Copyright Notice ---------------*/
        val copyrightNotice = findViewById<TextView>(R.id.copyright_notice)
        copyrightNotice.text = Helper.copyright(applicationContext)

        findViewById<View>(R.id.brand_dev).setOnClickListener { v: View? ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://brteam.tk")
            startActivity(intent)
        }



        /*---------------Notifications----------------*/
        binding.notiApp.setOnClickListener {
            //startActivity(Intent(this, NotificationsActivity::class.java))
        }

        /*---------------Logout----------------*/
        binding.logoutApp.setOnClickListener {
            Helper.logout(this)
        }

        /*---------------- Call Methods ---------------*/
        shareApp(shareApp)
        sendFeedback(sendFeedback)
        privacyPolicy(policyPrivacy)
        termsUse(policyTerms)
        aboutApp(aboutApp)


        /*---------------- Load Night Mode ---------------*/
        if (sharedPref!!.loadNightModeState()) {
            nightMode.isChecked = true
        }

        /*---------------- Load Screen On ---------------*/
        if (sharedPref!!.loadScreenState()) {
            screenOn.isChecked = true
        }

        /*---------------- Load Fullscreen ---------------*/
        if (sharedPref!!.loadFullscreenState()) {
            screenMode.isChecked = true
        }

        /*---------------- Load Notify Sound ---------------*/
        if (sharedPref!!.loadNotifySound()) {
            notifySound.isChecked = true
        }

        /*---------------- Load Notify Vibration ---------------*/
        if (sharedPref!!.loadNotifyVibration()) {
            notifyVibration.isChecked = true
        }

        /*---------------- Load Download Via Wifi ---------------*/
        if (sharedPref!!.loadDownloadWifi()) {
            downloadWifi.isChecked = true
        }

        /*---------------- Handle Night Mode ---------------*/
        nightMode.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            sharedPref!!.setNightModeState(isChecked)
            restartContext()
        }

        /*---------------- Handle Notify Sound  ---------------*/
        notifySound.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            sharedPref!!.setNotifySound(isChecked)
            restartContext()
        }

        /*---------------- Notify Vibration ---------------*/
        notifyVibration.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            sharedPref!!.setNotifyVibration(isChecked)
            restartContext()
        }

        /*---------------- Screen On ---------------*/
        screenOn.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            sharedPref!!.setScreenState(isChecked)
            restartContext()
        }


        /*---------------- Screen Mode ---------------*/
        screenMode.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            sharedPref!!.setFullscreenState(isChecked)
            restartContext()
        }


        /*---------------- Screen Mode ---------------*/
        downloadWifi.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            sharedPref!!.setDownloadWifi(isChecked)
            restartContext()
        }
    }

    /*---------------- Share App ---------------*/
    fun shareApp(shareApp: LinearLayout) {
        shareApp.setOnClickListener { v: View? ->
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "MiET BR Admin")
                var shareMessage = "\nLet me recommend you this application\n\n"
                shareMessage =
                    """
                ${shareMessage}https://play.google.com/store/apps/details?id=com.theelites.farmmit
               
                
                """.trimIndent()
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "choose one"))
            } catch (e: Exception) {
                //e.toString();
            }
        }
    }

    /*---------------- Send Feedback ---------------*/
    fun sendFeedback(sendFeedback: LinearLayout) {
        sendFeedback.setOnClickListener { v: View? ->
            val feedbackIntent = Intent(Intent.ACTION_SEND)
            feedbackIntent.type = "text/pain"

            // check gmail app if installed
            feedbackIntent.setPackage("com.google.android.gm")
            feedbackIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>("support@brsoftsol.com", "brijeshch80580@gmail.com"))
            feedbackIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                arrayOf("Feedback"))

            // try to open gmail app, if !not error will shown
            try {
                startActivity(feedbackIntent)
            } catch (ex: ActivityNotFoundException) {
                Helper.show_toast(
                    applicationContext,
                    "gmail not installed",
                    0,
                    1
                )
            }
        }
    }

    /*---------------- Privacy Policy ---------------*/
    fun privacyPolicy(policyPrivacy: LinearLayout) {
        policyPrivacy.setOnClickListener { v: View? ->
            Helper.show_toast(
                applicationContext,
                "Privacy Policy url",
                0,
                2
            )
        }
    }

    /*---------------- Terms of Use ---------------*/
    fun termsUse(policyTerms: LinearLayout) {
        policyTerms.setOnClickListener { v: View? ->
            Helper.show_toast(
                applicationContext,
                "Terms of use url",
                0,
                2
            )
        }
    }

    /*---------------- About App ---------------*/
    fun aboutApp(aboutApp: LinearLayout) {
        aboutApp.setOnClickListener { v: View? ->
            val aboutDialog = AboutDialog(this)
            aboutDialog.show()
        }
    }

    /*---------------- Restart App ---------------*/
    fun restartContext() {
        val restartActivity = Intent(applicationContext, SettingsActivity::class.java)
        restartActivity.putExtra("URL_TO_RESTORE", URL_TO_RESTORE)
        startActivity(restartActivity)
        finish()
    }
}