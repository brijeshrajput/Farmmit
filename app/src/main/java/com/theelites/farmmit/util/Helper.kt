package com.bunty.mietbradmin.Utils

import android.Manifest

import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.app.Activity
import kotlin.Throws
import android.content.pm.PackageManager
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.UiModeManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.google.android.gms.tasks.Task
import com.theelites.farmmit.BuildConfig
import com.theelites.farmmit.R
import com.theelites.farmmit.activities.LoginActivity
import com.theelites.farmmit.util.SharedPref
import java.text.SimpleDateFormat
import java.util.*

class Helper {
    companion object {
        /**
         * check for connection state whether user
         * connected to wifi or mobile network
         * or even not connected to internet
         * @param context for context
         */
        fun is_online(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var netInfo: NetworkInfo? = null
            if (cm != null) {
                netInfo = cm.activeNetworkInfo
            }
            return netInfo != null && netInfo.isConnectedOrConnecting
        }

        /**
         * logout from account and check
         * if account set by providers or
         * donut or firebase. set null to
         * shared preferences account
         * parameters and sign out if @firebase
         * @param context context
         */
        fun logout(context: Context) {
            // Initialize @shared_pref
            if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
                FirebaseAuth.getInstance().signOut()
            }
            context.startActivity(Intent(context, LoginActivity::class.java))
            (context as Activity).finishAffinity()
        }

        /**
         * use @get_version_code to get the app version code
         * and it requires only a
         * @param context context
         */
        @Throws(PackageManager.NameNotFoundException::class)
        fun get_version_name(context: Context): String {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.versionName
        }

        /**
         * use @show_toast to show a custom toast with
         * text, duration and style. for example:
         * show_toast(context, title, duration, type);
         * we assigned null to the layout inflater instead
         * of assigning @custom_taast to root as it will always
         * return null. Toast design is designed to be clean.
         * @param context for context
         * @param title for toast title
         * @param duration for duration, you can whether
         * choose 0 for short period or 1 for long period
         * @param type for toast type. currently support designs
         * is (0) #primary, (1) #alert, (2) #warning (3) #success
         */
        @SuppressLint("InflateParams")
        fun show_toast(context: Context, title: String?, duration: Int, type: Int) {
            // layout inflater
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var layout: View? = null
            if (type == 0) {
                layout =
                    Objects.requireNonNull(inflater).inflate(R.layout.toast_layout_primary, null)
            } else if (type == 1) {
                layout = Objects.requireNonNull(inflater).inflate(R.layout.toast_layout_alert, null)
            } else if (type == 2) {
                layout =
                    Objects.requireNonNull(inflater).inflate(R.layout.toast_layout_warning, null)
            } else if (type == 3) {
                layout =
                    Objects.requireNonNull(inflater).inflate(R.layout.toast_layout_success, null)
            }

            // initialize toast text and set text from the method
            val toast_title =
                Objects.requireNonNull(layout)?.findViewById<TextView>(R.id.toast_text)
            toast_title?.text = title

            // create a new toast and set gravity as @fill_horizontal
            // and @gravity.top to make the toast stick on the top.
            val toast = Toast(context)
            toast.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 0)
            toast.duration = duration
            toast.view = layout
            // show the toast
            toast.show()
        }

        /**
         * a method returns app copyright
         * @param context for context
         * @return to return the copyright notice
         */
        fun copyright(context: Context): String {
            val calendar = Calendar.getInstance()
            // get current year
            val year = calendar[Calendar.YEAR]
            // return copyright
            return "Copyright © " + year + " " + context.getString(R.string.app_name) + ". All Rights Reserved."
        }

        /**
         * use @get_formatted_date to format date
         * @param date_time for date time
         * @return formatted date
         */
        fun get_formatted_date(date_time: Long?): String {
            @SuppressLint("SimpleDateFormat") val newFormat = SimpleDateFormat("dd MMM yy hh:mm")
            return newFormat.format(Date(date_time!!))
        }

        /**
         * use @get_formatted_text to format text
         * @param text for text
         * @return formatted text
         */
        fun get_formatted_text(text: String): String {
            var text = text
            text = text.replace("<(.*?)>".toRegex(), " ")
            text = text.replace("<(.*?)\n".toRegex(), " ")
            text = text.replaceFirst("(.*?)>".toRegex(), " ")
            text = text.replace("&nbsp;".toRegex(), " ")
            text = text.replace("&amp;".toRegex(), " ")
            text = text.replace("&quot;".toRegex(), " ")
            text = text.replace("&#39;".toRegex(), " ")
            text = text.replace("&#8216;".toRegex(), "")
            text = text.replace("&#8217;".toRegex(), "")
            return text
        }

        /**
         * hide toolbar with animation
         * @param view for view
         */
        fun hide_toolbar(view: View) {
            val moveY = 2 * view.height
            view.animate()
                .translationY(-moveY.toFloat())
                .setDuration(300)
                .start()
        }

        /**
         * show toolbar with animation
         * @param view for view
         */
        fun show_toolbar(view: View) {
            view.animate()
                .translationY(0f)
                .setDuration(300)
                .start()
        }

        /**
         * a method that enables dark
         * mode when option is toggled
         * @param context for context
         */
        fun dark_mode(context: Context?) {
            val sharedPref: SharedPref
            sharedPref = SharedPref(context!!)
            // check if night mode @dark_mode is
            // enabled by user.
            // check if night mode @dark_mode is
            // enabled by user.
            val uiManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && sharedPref.loadNightModeState()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            } else if (sharedPref.loadNightModeState()) {
                uiManager.nightMode = UiModeManager.MODE_NIGHT_YES
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                uiManager.nightMode = UiModeManager.MODE_NIGHT_NO
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        /**
         * a method that enables fullscreen
         * mode when option is toggled
         * @param context for context
         */
        fun fullscreen_mode(context: Context) {
            val sharedPref: SharedPref
            sharedPref = SharedPref(context)
            // check if fullscreen mode @fullscreen_mode is
            // enabled by user.
            if (sharedPref.loadFullscreenState()) {
                (context as Activity).window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
        }

        /**
         * a method that enables screen
         * on mode when option is enabled
         * @param context for context
         */
        fun screen_state(context: Context) {
            val sharedPref: SharedPref
            sharedPref = SharedPref(context)
            // check if screen state @scree_on_state is
            // enabled by user.
            if (sharedPref.loadScreenState()) {
                (context as Activity).window.setFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                )
            }
        }

        /**
         * a method that enables notification
         * sound when open is enabled
         * @param context for context
         */
        fun notification_sound(context: Context?) {
            val sharedPref: SharedPref
            sharedPref = SharedPref(context!!)
            // check if notification sound is enabled by user
            //OneSignal.enableSound(!sharedPref.loadNotifySound());
        }

        /**
         * a method that enables notification
         * vibration when open is enabled
         * @param context for context
         */
        fun notification_vibration(context: Context?) {
            val sharedPref: SharedPref
            sharedPref = SharedPref(context!!)
            // check if notification vibration is enabled by user
            //OneSignal.enableVibrate(!sharedPref.loadNotifyVibration());
        }

        /**
         * verify storage permissions
         * @param activity for activity
         */
        fun verify_storage_permissions(activity: Activity?) {
            val REQUEST_EXTERNAL_STORAGE = 1
            val PERMISSIONS_STORAGE = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )

            // Check if we have read or write permission
            val writePermission = ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val readPermission =
                ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            val cameraPermission =
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
            if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED || cameraPermission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
            }
        }

        /**
         * verify external write storage permission
         * @param activity for activity
         */
        fun verify_external_write_permission(activity: Activity?): Boolean {
            val REQUEST_EXTERNAL_STORAGE = 1
            val PERMISSIONS_STORAGE = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

            // check write permission
            val writePermission = ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (writePermission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
            } else {
                return true
            }
            return false
        }

        /**
         * verify location permissions
         * @param activity for activity
         */
        fun verify_location_permissions(activity: Activity?) {
            val REQUEST_LOCATION = 1
            val PERMISSION_LOCATION = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            // Check if we have read or write permission
            val finePermission =
                ActivityCompat.checkSelfPermission(
                    activity!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            val coarsePermission =
                ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            if (finePermission != PackageManager.PERMISSION_GRANTED || coarsePermission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(activity, PERMISSION_LOCATION, REQUEST_LOCATION)
            }
        }

        /**
         * check application version then open
         * google play store if requires an update
         * @param context for context
         * @param version_code for version code
         */
        fun check_version(context: Context, version_code: Int) {
            try {
                val packageInfo = Objects.requireNonNull(context).packageManager.getPackageInfo(
                    context.packageName,
                    0
                )

                // check if version is greater than the app version
                // then request user to update the app from PlayStore
                val app_version_code = packageInfo.versionCode

                // check version
                if (version_code > app_version_code) {
                    val open_google_play = Intent(Intent.ACTION_VIEW)
                    open_google_play.data =
                        Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
                    open_google_play.setPackage("com.android.vending")
                    context.startActivity(open_google_play)
                } else {
                    show_toast(
                        context,
                        context.getString(R.string.you_are_using_the_latest_version),
                        0,
                        0
                    )
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
        }

        /**
         * firebase subscribe to topic
         * @param topic for topic name
         */
        fun firebase_subscribe_topic(topic: String) {
//        FirebaseMessaging.getInstance().subscribeToTopic(topic)
//            .addOnCompleteListener { task: Task<Void?> ->
//                var msg = "Subscribed to " + topic.toUpperCase()
//                if (!task.isSuccessful) {
//                    msg = "Error while subscribing to " + topic.toUpperCase()
//                }
//                Log.d("F Topic Subscribed: ", msg)
//            }
        }
    }
}