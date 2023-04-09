package com.theelites.farmmit.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import com.bunty.mietbradmin.Utils.Helper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.theelites.farmmit.App
import com.theelites.farmmit.models.MainJobModel

class MainViewModel(val data: String) : ViewModel() {
    // data present till activity destroy completely

    // data and fun - but not views
    val database = Firebase.database
    val myRef = database.getReference("Jobs/liste")
    private var auth: FirebaseAuth
    private var uid: String? = null

    private var mApp = App()

    init {
        auth = Firebase.auth
        val useruid = auth.currentUser?.uid
        uid = useruid
    }

    fun markJob(item : MainJobModel){
        if (item.ismark == true){
            item.ismark = false
        }else{
            item.ismark = true
        }

        myRef.child(uid!!).setValue(item)
    }

    fun formatDate(itemdate: String):String{
        return "09/04/2023"
    }

    fun itemClick(item : MainJobModel){

    }

    fun getDistanceInKm(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): String {
        //Log.d("Bunty getting", lat1.toString() + " " + lon1.toString() + " - " + lat2.toString() + " "+ lon2.toString());

        val startPoint = Location("locationA")
        startPoint.latitude = lat1
        startPoint.longitude = lon1

        val endPoint = Location("locationA")
        endPoint.latitude = lat2
        endPoint.longitude = lon2

        val d = startPoint.distanceTo(endPoint).toDouble()
        val dis = d/1000
        return dis.toString()
    }

    fun formatLocation(lat:String, lon:String):String{
//        if(!(lat.isNullOrEmpty()) && !(lon.isNullOrEmpty())){
//            return getDistanceInKm(lat.toDouble(), lon.toDouble(), mApp.curlat!!.toDouble(), mApp.curlong!!.toDouble())
//        }else{
//            return "0.05 Km"
//        }
        val rnds = (0..4).random()
        val arr = arrayListOf<String>("0.01 KM", "0.00 KM", "0.02 KM", "0.00 KM", "0.30 KM")

        return arr.get(rnds)

    }
}