package com.theelites.farmmit.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bunty.mietbradmin.Utils.Helper
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.theelites.farmmit.R
import com.theelites.farmmit.databinding.ActivityProfileBinding
import com.theelites.farmmit.models.ProfileModel
import com.theelites.farmmit.util.RequestDialog
import com.theelites.farmmit.util.SharedPref
import java.util.ArrayList

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var auth: FirebaseAuth = Firebase.auth
    private var PICK_IMAGE_REQUEST = 123
    val database = Firebase.database
    val myRef = database.getReference("Users")
    private var image_path: Uri? = null
    private var firebaseStorage= FirebaseStorage.getInstance()
    private var storageRef: StorageReference = firebaseStorage.getReference()
    private var uid: String? = null
    private lateinit var requestDialog: RequestDialog
    private var linkurl:String? = null

    private var latitudee:String? = null
    private var longitudee:String? = null

    private var addres:String? = null

    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var curlat: String? = null
    var curlong: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Helper.dark_mode(this)
        Helper.fullscreen_mode(this)
        Helper.notification_sound(this)
        Helper.notification_vibration(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@ProfileActivity, R.layout.activity_profile)

        requestDialog = RequestDialog(this)

        val useremail = auth.currentUser?.email
        val useruid = auth.currentUser?.uid
        uid = useruid
        if (!useremail.isNullOrEmpty()){
            binding.emailu.setText(useremail)
        }

        val pic = binding.profileImg
        storageRef.child("ProfiePic").child(useruid!!)
            .downloadUrl.addOnSuccessListener { uri ->
                Picasso.get().load(uri).into(pic)
            }

        binding.btnupdate.setOnClickListener {
            saveData()
        }

        binding.bckbtn.setOnClickListener {
            super.onBackPressed()
        }

        pic.setOnClickListener {
            fileChosser()
        }

        //Location
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this@ProfileActivity)
        if (ActivityCompat.checkSelfPermission(
                this@ProfileActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this@ProfileActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            currentLocation
        } else {
            ActivityCompat.requestPermissions(
                this@ProfileActivity, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 100
            )
        }

    }

    fun saveData(){
        if (addres.isNullOrEmpty()){
            addres = "No Address"
        }
        myRef.child(uid!!).setValue(ProfileModel(binding.emailu.text.toString(),
            binding.fname.text.toString(),
            binding.phone.text.toString(),
            addres,
            1,
            linkurl,
            curlat,
            curlong
        )).addOnSuccessListener {
                Helper.show_toast(baseContext, "Profile Updated", 0,3)
        }.addOnFailureListener {
            Helper.show_toast(baseContext, "Profile Updation Failed", 0,1)
        }
    }

    private fun fileChosser(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null){
            requestDialog.show()
            image_path = data.data
            val pic = findViewById<ImageView>(R.id.profile_img)
            if (image_path!=null){
                //pic.setImageURI(image_path)
                Picasso.get().load(image_path).into(pic)
            }

            val imgRef = storageRef.child("ProfiePic").child(uid!!)
            val uploadImg = imgRef.putFile(image_path!!)
            uploadImg.addOnCompleteListener {
                requestDialog.dismiss()
                Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
            }
            uploadImg.addOnFailureListener { e->
                requestDialog.dismiss()
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            uploadImg.addOnSuccessListener {
                imgRef.downloadUrl.addOnSuccessListener {
                    linkurl = it.toString()
                }
            }
        }
    }



    private fun getUserData(email: String){

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
            Toast.makeText(this@ProfileActivity, "Permission Not Granted", Toast.LENGTH_SHORT).show()
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

    private fun getDistanceInKm(
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



}