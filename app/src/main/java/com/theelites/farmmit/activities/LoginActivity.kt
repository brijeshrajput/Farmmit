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
import com.theelites.farmmit.databinding.ActivityLoginBinding
import com.theelites.farmmit.util.RequestDialog

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var requestDialog: RequestDialog

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        auth = Firebase.auth
        requestDialog = RequestDialog(this)

        binding.btnLogin.setOnClickListener {
            if (binding.editTextTextEmailAddress.text.isNullOrEmpty()){
                binding.editTextTextEmailAddress.error = "Please enter Email !"
            }
            else if (binding.editTextTextPassword.text.isNullOrEmpty()){
                binding.editTextTextPassword.error = "Please enter Password !"
            }else{
                requestDialog.show()
                loginuser(binding.editTextTextEmailAddress.text.trim().toString(), binding.editTextTextPassword.text.trim().toString())
            }
        }
        binding.btnReg.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btnForgetpass.setOnClickListener {
            startActivity(Intent(this, ForgetPassActivity::class.java))
        }

        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            for (key in extras.keySet()) {
                val value = extras[key]
                Log.d(
                    "GOOGLE_FIREBASE",
                    "Extras received at onCreate: Key: $key Value: $value"
                )
            }
            val title = extras.getString("title")
            val message = extras.getString("body")
            if (title!=null && message != null && message.length > 0) {
                getIntent().removeExtra("body")
                saveNotification(title, message)
            }
        }


    }

    fun loginuser(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Helper.show_toast(this, "Welcome: Login Success", 0, 3)
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

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        Log.i("Bunty", "$currentUser")

        if(currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            //checkVersion(2, 1)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("NEW_INTENT", "On new intent started")

        val extras = intent!!.extras
        if (extras != null) {
            for (key in extras.keySet()) {
                val value = extras[key]
                Log.d(
                    "GOOGLE_FIREBASE",
                    "Extras received at onCreate: Key: $key Value: $value"
                )
            }
            val title = extras.getString("title")
            val message = extras.getString("body")
            if (title!=null && message != null && message.length > 0) {
                getIntent().removeExtra("body")
                saveNotification(title, message)
            }
        }
    }

    /**
     * request save notification into room db
     * @param title for notification title
     * @param message for notification message
     */
    private fun saveNotification(title: String, message: String) {
//        val dao = NotificationsDatabase.requestDatabase(this).requestDAO()
//        val notification = Notification()
//        notification.title = title
//        notification.content = message
//        notification.id = System.currentTimeMillis()
//        notification.created_at = System.currentTimeMillis()
//        notification.read = false
//        dao.requestInsertNotification(notification)
    }
}