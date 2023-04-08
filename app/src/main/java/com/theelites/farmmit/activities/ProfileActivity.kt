package com.theelites.farmmit.activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.*
import androidx.databinding.DataBindingUtil
import com.bunty.mietbradmin.Utils.Helper
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.theelites.farmmit.R
import com.theelites.farmmit.databinding.ActivityProfileBinding
import com.theelites.farmmit.util.RequestDialog
import com.theelites.farmmit.util.SharedPref

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    lateinit var sectn: ArrayList<String>
    lateinit var db:FirebaseFirestore
    lateinit var pref: SharedPreferences
    private var PICK_IMAGE_REQUEST = 123
    private var image_path: Uri? = null
    private lateinit var firebaseStorage:FirebaseStorage
    private lateinit var storageRef: StorageReference
    private var uid: String? = null
    lateinit var sf:SharedPref
    private lateinit var requestDialog: RequestDialog
    private lateinit var branchlist:ArrayList<String>
    private lateinit var yearlist:ArrayList<String>
    private lateinit var grps: ArrayList<String>

    private lateinit var email: EditText
    private lateinit var fname : EditText
    private lateinit var phon : EditText
    private lateinit var corsSp : MaterialAutoCompleteTextView
    private lateinit var brnchSp : MaterialAutoCompleteTextView
    private lateinit var yearSp : MaterialAutoCompleteTextView
    private lateinit var secSp: MaterialAutoCompleteTextView
    private lateinit var grpSp: MaterialAutoCompleteTextView
    private lateinit var dobSp: MaterialAutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        Helper.dark_mode(this)
        Helper.fullscreen_mode(this)
        Helper.notification_sound(this)
        Helper.notification_vibration(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@ProfileActivity, R.layout.activity_profile)

        auth = Firebase.auth
        firebaseStorage = FirebaseStorage.getInstance()
        storageRef = firebaseStorage.getReference()
        db = Firebase.firestore
        sf = SharedPref(this)
        requestDialog = RequestDialog(this)

        email = findViewById(R.id.emailu)
        fname = findViewById(R.id.fnameu)
        phon = findViewById(R.id.phnnou)
        corsSp = findViewById(R.id.corsu)
        brnchSp = findViewById(R.id.brnchu)
        yearSp = findViewById(R.id.yearu)
        secSp = findViewById(R.id.secu)
        grpSp = findViewById(R.id.grpu)
        dobSp = findViewById(R.id.dobu)

        val useremail = auth.currentUser?.email
        val useruid = auth.currentUser?.uid
        uid = useruid
        if (!useremail.isNullOrEmpty()){
            email.setText(useremail)
        }

        val insertT: Thread
        insertT = Thread {
            if (!useremail.isNullOrEmpty()){
                getUserData(useremail)
            }
        }
        insertT.start()
        try {
            insertT.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        branchlist = ArrayList()
        yearlist = ArrayList()
        sectn = ArrayList()
        grps = ArrayList()

        //fillLocalData()

        // ------------Spinners--------------
        val adptr1 = ArrayAdapter(this, R.layout.drop_down_option, MyConstansts().course)
        corsSp.setAdapter(adptr1)
        corsSp.setOnItemClickListener { parent, view, position, id ->
            branchlist.clear()
            yearlist.clear()
            when(parent.getItemAtPosition(position)){
                "B.Tech" -> {
                    branchlist.addAll(MyConstansts().btech)
                    yearlist.addAll(MyConstansts().myear)
                }
                "B.PHARMA" -> { branchlist.add("Default Branch")
                    yearlist.addAll(MyConstansts().myear3) }
                "M.TECH"-> { branchlist.addAll(MyConstansts().mtech)
                    yearlist.addAll(MyConstansts().myear2) }
                "MBA"-> { branchlist.add("Default Branch")
                    yearlist.addAll(MyConstansts().myear2) }
                "MCA"-> { branchlist.add("Default Branch")
                    yearlist.addAll(MyConstansts().myear2) }
                "M.PHARMA"-> { branchlist.addAll(MyConstansts().mpharma)
                    yearlist.addAll(MyConstansts().myear2) }
                else -> { branchlist.add("Default Branch")
                    yearlist.addAll(MyConstansts().myear) }
            }
            //corspos = position.toString()
        }

        val adptr2 = ArrayAdapter(this, R.layout.drop_down_option, branchlist)
        adptr2.setNotifyOnChange(true)
        brnchSp.setAdapter(adptr2)
        brnchSp.setOnItemClickListener { parent, view, position, id ->
            //corspos = position.toString()
        }
        brnchSp.setOnClickListener {
            if (branchlist.isEmpty()){
                Helper.show_toast(this, "Please reselect your Course first !!", 1, 2)
            }
        }

        val adptr3 = ArrayAdapter(this, R.layout.drop_down_option, yearlist)
        yearSp.setAdapter(adptr3)
        yearSp.setOnItemClickListener { parent, view, position, id ->
            grps.clear()
            getSections(parent.getItemAtPosition(position).toString())
            if (parent.getItemAtPosition(position).toString().equals("1 Year")){
                grps.addAll(MyConstansts().grp1)
            }else{
                grps.addAll(MyConstansts().grp2)
            }
        }
        yearSp.setOnClickListener {
            if (yearlist.isEmpty()){
                Helper.show_toast(this, "Please reselect your Course and Branch first !!", 1, 2)
            }
        }

        val adptr4 = ArrayAdapter(this, R.layout.drop_down_option, sectn)
        secSp.setAdapter(adptr4)
        secSp.setOnItemClickListener { parent, view, position, id ->
        }
        secSp.setOnClickListener {
            if (sectn.isEmpty()){
                Helper.show_toast(this, "Please wait we are fetching sections for you", 1, 2)
            }
            Handler().postDelayed(
                Runnable {
                    if (sectn.isEmpty()){
                        Helper.show_toast(this, "Please reselect your Course, Branch and Year.", 1, 1)
                    }
                }, 3000
            )
        }

        val adptr5 = ArrayAdapter(this, R.layout.drop_down_option, grps)
        grpSp.setAdapter(adptr5)
        grpSp.setOnItemClickListener { parent, view, position, id ->
        }

        val btn = findViewById<Button>(R.id.btnupdate)
        val pic = findViewById<ImageView>(R.id.profile_img)
        val bck = findViewById<ImageButton>(R.id.bckbtn)


        storageRef.child("ProfiePic").child(useruid!!)
            .downloadUrl.addOnSuccessListener { uri ->
                Picasso.get().load(uri).into(pic)
            }

        btn.setOnClickListener {
            val em = email.text.trim().toString()
            val fnam = fname.text.trim().toString()
            val phn = phon.text.trim().toString()
            val corse = corsSp.text.toString()
            val brnch = brnchSp.text.toString()
            val yrs = yearSp.text.toString()
            val sec = secSp.text.toString()
            val dob = dobSp.text.trim().toString()
            val grp = grpSp.text.toString()

            if (corse.isEmpty()) {
                corsSp.error = "Select Course"
            }else if(brnch.isEmpty()) {
                brnchSp.error = "Select Branch"
            }else if(yrs.isEmpty()) {
                yearSp.error = "Select Year"
            }else if(sec.isEmpty()) {
                secSp.error = "Select Section"
            }else{
                requestDialog.show()
                sendData(useruid, em, fnam, phn, corse, brnch, yrs, sec, dob, grp)
            }
        }

        bck.setOnClickListener {
            super.onBackPressed()
        }

        pic.setOnClickListener {
            fileChosser()
        }

    }

    fun sendData(uid: String, email: String, name: String, phone: String, course: String, branch: String, year:String, section: String, dob:String?, group:String?){
        setLocalData(uid,email,name,phone,course,branch,year,section,dob,group)
        val map = hashMapOf(
            "uid" to uid,
            "email" to email,
            "name" to name,
            "phone" to phone,
            "course" to course,
            "branch" to branch,
            "year" to year,
            "section" to section,
            "dob" to dob,
            "group" to group
        )

        db.collection("Students")
            .document(email)
            .set(map, SetOptions.merge())
            .addOnSuccessListener {
                requestDialog.dismiss()
                Helper.show_toast(this@ProfileActivity, "Success: Profile Updated", 1, 3)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                requestDialog.dismiss()
                Helper.show_toast(this@ProfileActivity, "Error: Failed with ${e.message.toString()}", 1, 1)
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
        }
    }

    private fun getSections(year: String){
        sectn.clear()
        db.collection("Sections")
            .whereEqualTo("year", year)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    document.getField<String>("name")?.let { sectn.add(it) }
                }
            }
            .addOnFailureListener { exception ->
                Helper.show_toast(this, "Error: ${exception.message.toString()}", 0, 1)
            }
    }

    private fun getUserData(email: String){
        db.collection("Students")
            .document(email)
            .get()
            .addOnSuccessListener { result ->
                if (!result.data.isNullOrEmpty()){
                    result.getField<String>("name")?.let { sf.setUserFullName(it) }
                    result.getField<String>("phone")?.let { sf.setUserPhone(it) }
                    result.getField<String>("course")?.let { sf.setUserCourse(it) }
                    result.getField<String>("branch")?.let { sf.setUserBranch(it) }
                    result.getField<String>("year")?.let { sf.setUserYear(it) }
                    result.getField<String>("section")?.let { sf.setUserSection(it) }
                    result.getField<String>("dob")?.let { sf.setUserDOB(it) }
                    result.getField<String>("group")?.let { sf.setUserGroup(it) }
                    fillLocalData()
                }
            }
            .addOnFailureListener { exception ->
                Helper.show_toast(this, "Error: ${exception.message.toString()}", 0, 1)
            }
    }

    fun setLocalData(uid: String, email: String, name: String, phone: String, course: String, branch: String, year:String, section: String, dob:String?, group:String?){
        sf.setUserId(uid)
        sf.setUserEmail(email)
        sf.setUserFullName(name)
        sf.setUserPhone(phone)
        sf.setUserCourse(course)
        sf.setUserBranch(branch)
        sf.setUserYear(year)
        sf.setUserSection(section)
        if (!dob.isNullOrEmpty()){
            sf.setUserDOB(dob)
        }
        if (!group.isNullOrEmpty()){
            sf.setUserGroup(group)
        }
    }

    fun fillLocalData(){
        fname.setText(sf.loadUserFullName())
        phon.setText(sf.loadUserPhone())
        corsSp.setText(sf.loadUserCourse(), false)
        brnchSp.setText(sf.loadUserBranch(), false)
        yearSp.setText(sf.loadUserYear(), false)
        secSp.setText(sf.loadUserSection(), false)
        dobSp.setText(sf.loadUserDOB())
        grpSp.setText(sf.loadUserGroup(), false)
    }
}