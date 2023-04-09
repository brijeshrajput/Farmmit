package com.theelites.farmmit.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bunty.mietbradmin.Utils.Helper
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.theelites.farmmit.R
import com.theelites.farmmit.databinding.ActivityJobCreateBinding
import com.theelites.farmmit.models.CreateCategoryModel
import com.theelites.farmmit.models.CreateJobModel
import com.theelites.farmmit.models.MainJobModel

class JobCreateActivity : AppCompatActivity() {

    lateinit var binding: ActivityJobCreateBinding
    lateinit var createJobModel: CreateJobModel
    private lateinit var auth: FirebaseAuth
    private var uid: String? = null
    val database = Firebase.database
    val myRef = database.getReference("Jobs")
    val adapterArr = arrayListOf<String>()

    private var latitudee:String? = null
    private var longitudee:String? = null

    private var addres:String? = null

    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var curlat: String? = null
    var curlong: String? = null

    private val user = Firebase.auth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@JobCreateActivity, R.layout.activity_job_create)

        binding.createcategory.setOnClickListener {
            startActivity(Intent(this@JobCreateActivity, CreateCategoryActivity::class.java))
        }

        auth = Firebase.auth
        val useruid = auth.currentUser?.uid
        uid = useruid

        getAdapterdata()

        val adptr1 = ArrayAdapter(this, R.layout.drop_down_option, adapterArr)
        binding.reqCategory.setAdapter(adptr1)

        binding.btnupdate.setOnClickListener {
            savedata()
        }

        binding.bckbtn.setOnClickListener {
            super.onBackPressed()
        }

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this@JobCreateActivity)
        if (ActivityCompat.checkSelfPermission(
                this@JobCreateActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this@JobCreateActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            currentLocation
        } else {
            ActivityCompat.requestPermissions(
                this@JobCreateActivity, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 100
            )
        }


    }

    private fun getAdapterdata() {
        myRef.child("categories").addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
//                    val obj = arrayListOf<CreateCategoryModel>()
                    for (data in snapshot.children){
//                        data.getValue(CreateCategoryModel::class.java)?.let { obj.add(it) }
                        adapterArr.add(data.child("categoryName").value.toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Helper.show_toast(baseContext, "Error Occured", 0,1)
                }

            }
        )

    }

    private fun savedata(){
        val rnds = (0..1000).random()
        myRef.child("liste").child(uid!!).setValue(MainJobModel(
            rnds.toString(),
            binding.reqCategory.text.toString(),
            "url",
            "11",
            "Full/Part time",
            "address",
            "address",
            binding.jobName.text.toString(),
            "desc",
            "how to apply",
            "https://firebasestorage.googleapis.com/v0/b/farmmit-bf2b6.appspot.com/o/Category%2Fv27e5lKX1HZ4LKvUKTp3kCzZDjf1?alt=media&token=b9208590-25f9-4dcf-ac51-ba0805281d07",
            false,
            curlat,
            curlong
        )).addOnSuccessListener {
            Helper.show_toast(baseContext, "Job Created", 0, 3)
        }.addOnFailureListener {
            Helper.show_toast(baseContext, "Error Occured", 0, 1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.size > 0 && (grantResults[0] + grantResults[1]
                    == PackageManager.PERMISSION_GRANTED)
        ) {
            currentLocation
        } else {
            Toast.makeText(this@JobCreateActivity, "Permission Not Granted", Toast.LENGTH_SHORT).show()
        }
    }

    // Check Distance
    @get:SuppressLint("MissingPermission")
    private val currentLocation: Unit
        get() {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            ) {
                fusedLocationProviderClient?.lastLocation?.addOnCompleteListener { task: Task<Location?> ->
                    val location = task.result
                    if (location != null) {
                        curlong = location.longitude.toString()
                        curlat = location.latitude.toString()
                        //getting loc
                        addres = curlat+", "+curlong
                    } else {
                        val locationRequest = LocationRequest()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                            .setInterval(10000)
                            .setFastestInterval(1000)
                            .setNumUpdates(1)
                        val locationCallback: LocationCallback = object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                val location1 = locationResult.lastLocation
                                curlong = location1.longitude.toString()
                                curlat = location1.latitude.toString()
                                // geting loc
                                addres = curlat+", "+curlong
                            }
                        }
                        fusedLocationProviderClient!!.requestLocationUpdates(
                            locationRequest,
                            locationCallback, Looper.myLooper()
                        )
                    }
                    // Check Distance

                }
            } else {
                startActivity(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
}