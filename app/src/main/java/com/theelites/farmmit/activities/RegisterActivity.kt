package com.theelites.farmmit.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.bunty.mietbradmin.Utils.Helper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.theelites.farmmit.R
import com.theelites.farmmit.databinding.ActivityRegisterBinding
import com.theelites.farmmit.util.RequestDialog

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    lateinit var requestDialog: RequestDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        auth = Firebase.auth
        requestDialog = RequestDialog(this)

        binding.regBtn.setOnClickListener {
            if (binding.editTextTextEmailAddress.text.isNullOrEmpty()){
                binding.editTextTextEmailAddress.error = "Please enter Email !"
            }
            else if (binding.editTextTextPassword.text.isNullOrEmpty()){
                binding.editTextTextPassword.error = "Please enter Password !"
            }else{
                createUser(binding.editTextTextEmailAddress.text.toString(), binding.editTextTextPassword.text.toString())
            }
        }
        binding.loginBtn.setOnClickListener {
            requestDialog.show()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    fun createUser(email:String, password:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Helper.show_toast(this, "Welcome: Registeration Success", 0, 3)
                    requestDialog.dismiss()
                    startActivity(Intent(this, TaskActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    requestDialog.dismiss()
                    Helper.show_toast(this, "Authentication failed: "+ task.exception?.message.toString(), 0, 1)
                }
            }
    }

    fun mietcheckup(email: String):Boolean{
        return email.contains("@miet.ac.in", true)
        //return true
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        Log.i("Bunty", "$currentUser")

        if(currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}