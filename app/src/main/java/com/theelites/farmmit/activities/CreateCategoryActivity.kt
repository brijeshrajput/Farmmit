package com.theelites.farmmit.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bunty.mietbradmin.Utils.Helper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.theelites.farmmit.R
import com.theelites.farmmit.databinding.ActivityCreateCategoryBinding
import com.theelites.farmmit.models.CreateCategoryModel
import com.theelites.farmmit.util.RequestDialog

class CreateCategoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateCategoryBinding
    private lateinit var auth: FirebaseAuth
    val database = Firebase.database
    val myRef = database.getReference("Jobs/categories")
    private var PICK_IMAGE_REQUEST = 123
    private lateinit var requestDialog: RequestDialog
    private var image_path: Uri? = null
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private var uid: String? = null
    private var linkurl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_category)

        auth = Firebase.auth
        requestDialog = RequestDialog(this)
        firebaseStorage = FirebaseStorage.getInstance()
        storageRef = firebaseStorage.getReference()

        val useruid = auth.currentUser?.uid
        uid = useruid


        binding.btnupdate.setOnClickListener {
            uploaddata()
        }

        binding.layot3.setOnClickListener {
            fileChosser()
        }

        binding.bckbtn.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun uploaddata(){
        val createCategoryModel = CreateCategoryModel(binding.categoryName.text.toString(), linkurl)

        val rnds = (0..1000).random()
        myRef.child(rnds.toString()).setValue(createCategoryModel)
        Log.e("Bunty", createCategoryModel.categoryImage.toString())
        Helper.show_toast(baseContext, "Category Uploaded", 0,3)

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

            val imgRef = storageRef.child("Category").child(uid!!)
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
}