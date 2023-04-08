package com.theelites.farmmit.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bunty.mietbradmin.Utils.Helper
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.theelites.farmmit.R
import com.theelites.farmmit.databinding.ActivityForgetPassBinding

class ForgetPassActivity : AppCompatActivity() {
    lateinit var binding: ActivityForgetPassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_forget_pass)

        binding.btnForgetpass.setOnClickListener {
            if (binding.edtEmail.text.isEmpty()){
                binding.edtEmail.error = "Enter email !"
            }else{
                forgetPass(binding.edtEmail.text.toString().trim())
            }
        }

        binding.lgnbtn.setOnClickListener {
            super.onBackPressed()
        }

    }

    fun forgetPass(email:String){
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Helper.show_toast(this, "Email of Reset link is sent to you college email.", 0, 3)
                }
            }
    }
}