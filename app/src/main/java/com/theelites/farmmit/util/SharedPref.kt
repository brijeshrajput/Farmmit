package com.theelites.farmmit.util

import android.content.Context
import android.content.SharedPreferences
import com.bunty.mietbradmin.Utils.Exceptions

class SharedPref(context: Context) {
    private val sharedPreferences: SharedPreferences

    /*---------------- Set User ID ---------------*/
    fun setUserId(state: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("UserId", state)
        editor.apply()
    }

    /*---------------- Load User ID ---------------*/
    fun loadUserId(): String? {
        return sharedPreferences.getString("UserId", Exceptions.EXCEPTION_ERROR_USER_ID)
    }

    /*---------------- Set Account Provider ---------------*/
    fun setAccountProvider(state: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("AccountProvider", state)
        editor.apply()
    }

    /*---------------- Set Score ---------------*/
    fun setScores(totalQues: String?, correctQues: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("totalQues", totalQues)
        editor.putString("correctQues", correctQues)
        editor.apply()
    }

    /*---------------- Load Score ---------------*/
    fun loadTotalQues(): String? {
        return sharedPreferences.getString("totalQues", "00")
    }

    fun loadCorrectQues(): String? {
        return sharedPreferences.getString("correctQues", "00")
    }

    /*---------------- Load Account Provider ---------------*/
    fun loadAccountProvider(): String? {
        return sharedPreferences.getString(
            "AccountProvider",
            Exceptions.EXCEPTION_ERROR_ACCOUNT_PROVIDER
        )
    }

    /*---------------- Set User Name ---------------*/
    fun setUserName(state: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("UserName", state)
        editor.apply()
    }

    /*---------------- Load User Name ---------------*/
    fun loadUserName(): String? {
        return sharedPreferences.getString("UserName", Exceptions.EXCEPTION_ERROR_USER_NAME)
    }

    /*---------------- Set User Phone ---------------*/
    fun setUserPhone(state: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("UserPhone", state)
        editor.apply()
    }

    /*---------------- Load User Phone ---------------*/
    fun loadUserPhone(): String? {
        return sharedPreferences.getString("UserPhone", "+91")
    }

    /*---------------- Set User Email ---------------*/
    fun setUserEmail(state: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("UserEmail", state)
        editor.apply()
    }

    /*---------------- Load User Email ---------------*/
    fun loadUserEmail(): String? {
        return sharedPreferences.getString("UserEmail", Exceptions.EXCEPTION_ERROR_USER_EMAIL)
    }

    /*---------------- Set User Image ---------------*/
    fun setUserImage(state: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("UserImage", state)
        editor.apply()
    }

    /*---------------- Load User Image ---------------*/
    fun loadUserImage(): String? {
        return sharedPreferences.getString("UserImage", Exceptions.EXCEPTION_ERROR_USER_IMAGE)
    }

    /*---------------- Set User School ---------------*/
    fun setUserSchool(state: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("UserSchool", state)
        editor.apply()
    }

    /*---------------- Load User School ---------------*/
    fun loadUserSchool(): String? {
        return sharedPreferences.getString("UserSchool", "Enter School")
    }

    /*---------------- Set User Class ---------------*/
    fun setUserClass(state: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("UserClass", state)
        editor.apply()
    }

    /*---------------- Load User Class ---------------*/
    fun loadUserClass(): String? {
        return sharedPreferences.getString("UserClass", "Enter Class")
    }

    /*---------------- Set Config Branch ---------------*/
    fun setConfigBranch(state: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("ConfigBranch", state)
        editor.apply()
    }

    /*---------------- Load User Branch ---------------*/
    fun loadConfigBranch(): String? {
        return sharedPreferences.getString("ConfigBranch", "CSE Ai&Ml")
    }

    /*---------------- Set Config Course ---------------*/
    fun setConfigCourse(state: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("ConfigCourse", state)
        editor.apply()
    }

    /*---------------- Load User Course ---------------*/
    fun loadConfigCourse(): String? {
        return sharedPreferences.getString("ConfigCourse", "B.Tech")
    }

    /*---------------- Set NightMode ---------------*/
    fun setNightModeState(state: Boolean?) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("NightMode", state!!)
        editor.apply()
    }

    /*---------------- Load NightMode ---------------*/
    fun loadNightModeState(): Boolean {
        return sharedPreferences.getBoolean("NightMode", false)
    }

    /*---------------- Set NightMode ---------------*/
    fun setGesturesModeState(state: Boolean?) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("GesturesMode", state!!)
        editor.apply()
    }

    /*---------------- Load NightMode ---------------*/
    fun loadGesturesModeState(): Boolean {
        return sharedPreferences.getBoolean("GesturesMode", false)
    }

    /*---------------- Set Fullscreen ---------------*/
    fun setFullscreenState(state: Boolean?) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("Fullscreen", state!!)
        editor.apply()
    }

    /*---------------- Load Fullscreen ---------------*/
    fun loadFullscreenState(): Boolean {
        return sharedPreferences.getBoolean("Fullscreen", false)
    }

    /*---------------- Set Screen On ---------------*/
    fun setScreenState(state: Boolean?) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("ScreenOn", state!!)
        editor.apply()
    }

    /*---------------- Load Screen On ---------------*/
    fun loadScreenState(): Boolean {
        return sharedPreferences.getBoolean("ScreenOn", false)
    }

    /*---------------- Set Notify Sound ---------------*/
    fun setNotifySound(state: Boolean?) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("NotificationSound", state!!)
        editor.apply()
    }

    /*---------------- Load Notify Sound ---------------*/
    fun loadNotifySound(): Boolean {
        return sharedPreferences.getBoolean("NotificationSound", false)
    }

    /*---------------- Set Notify Vibration ---------------*/
    fun setNotifyVibration(state: Boolean?) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("NotificationVibration", state!!)
        editor.apply()
    }

    /*---------------- Load Notify Vibration ---------------*/
    fun loadNotifyVibration(): Boolean {
        return sharedPreferences.getBoolean("NotificationVibration", false)
    }

    /*---------------- Set Download Wifi ---------------*/
    fun setDownloadWifi(state: Boolean?) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("DownloadWifi", state!!)
        editor.apply()
    }

    /*---------------- Load Download Wifi ---------------*/
    fun loadDownloadWifi(): Boolean {
        return sharedPreferences.getBoolean("DownloadWifi", false)
    }

    /*---------------- Set FCM Register ID ---------------*/
    fun setFCMRregisterID(state: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("FCMRegisterID", state)
        editor.apply()
    }

    /*---------------- Load FCM Register ID ---------------*/
    fun loadFCMRegisterID(): String? {
        return sharedPreferences.getString("FCMRegisterID", "")
    }

    /*---------------- Set Need Register ---------------*/
    fun setNeedRegister(state: Boolean?) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("NeedRegister", state!!)
        editor.apply()
    }

    /*---------------- Load Need Register  ---------------*/
    fun loadNeedRegister(): Boolean {
        return sharedPreferences.getBoolean("NeedRegister", false)
    }

    /*---------------- Set Subscribe Notifications ---------------*/
    fun setSubscribeNotifications(state: Boolean?) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("SubscribeNotifications", state!!)
        editor.apply()
    }

    /*---------------- Load Subscribe Notifications ---------------*/
    fun loadSubscribeNotifications(): Boolean {
        return sharedPreferences.getBoolean("SubscribeNotifications", false)
    }

    /*---------------- Shared Pref ---------------*/
    init {
        sharedPreferences = context.getSharedPreferences("Shared Preferences", Context.MODE_PRIVATE)
    }
}