package com.theelites.farmmit

import android.app.Application

class App: Application() {
    var curlat:String? = null
    var curlong:String? = null
    override fun onCreate() {
        super.onCreate()
    }
}