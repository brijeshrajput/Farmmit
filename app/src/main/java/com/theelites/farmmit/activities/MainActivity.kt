package com.theelites.farmmit.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bunty.mietbradmin.Utils.Helper
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.theelites.farmmit.App
import com.theelites.farmmit.R
import com.theelites.farmmit.adapters.MainAdapter
import com.theelites.farmmit.adapters.MainMarkedAdapter
import com.theelites.farmmit.databinding.ActivityMainBinding
import com.theelites.farmmit.models.MainJobModel
import com.theelites.farmmit.viewmodel.MainViewModel
import com.theelites.farmmit.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel:MainViewModel
    private var mainJobList = mutableListOf<MainJobModel>()
    private var markedJobList = mutableListOf<MainJobModel>()
    val database = Firebase.database
    val myRef = database.getReference("Jobs")
    lateinit var adapter:MainAdapter
    lateinit var adapter2: MainMarkedAdapter

    private var latitudee:String? = null
    private var longitudee:String? = null

    private var addres:String? = null

    val mApp = App()

    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var curlat: String? = null
    var curlong: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //data binding
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)

        // ViewModel
        mainViewModel = ViewModelProvider(this, MainViewModelFactory("Bunty")).get(MainViewModel::class.java)

        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this

        binding.notif.setOnClickListener {
            startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
        }

        Helper.verify_location_permissions(this@MainActivity)

        val rv = binding.recyclerView
        rv.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(mainJobList, mainViewModel)
        rv.adapter = adapter

        val rv2 = binding.recyclerViewMarked
        rv2.layoutManager = LinearLayoutManager(this)
        adapter2 = MainMarkedAdapter(markedJobList, mainViewModel)
        rv2.adapter = adapter2




        //Location
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this@MainActivity)
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            currentLocation
        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 100
            )
        }

        getData()
        getMarkedata()

    }

    private fun getData(){
        myRef.child("liste").addValueEventListener(
            object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
//                    val obj = arrayListOf<CreateCategoryModel>()
                    mainJobList.clear()
                    for (data in snapshot.children){
                        data.getValue(MainJobModel::class.java)?.let { mainJobList.add(it) }
                    }
                    //Log.e("Bunty", mainJobList.toString())
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Helper.show_toast(baseContext, "Error Occured", 0,1)
                }

            }
        )
    }

    private fun getMarkedata(){
        myRef.child("liste").addValueEventListener(
            object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
//                    val obj = arrayListOf<CreateCategoryModel>()
                    markedJobList.clear()
                    for (data in snapshot.children){
                        Log.e("Bunty", data.value.toString())
                        if(data.child("ismark").value as Boolean == true){
                            data.getValue(MainJobModel::class.java)?.let { markedJobList.add(it) }
                        }

                    }
                    //Log.e("Bunty", markedJobList.toString())
                    adapter2.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Helper.show_toast(baseContext, "Error Occured", 0,1)
                }

            }
        )
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
            Toast.makeText(this@MainActivity, "Permission Not Granted", Toast.LENGTH_SHORT).show()
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
                        mApp.curlat = curlat
                        mApp.curlong = curlong

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
                                mApp.curlat = curlat
                                mApp.curlong = curlong
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