package com.theelites.farmmit.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.theelites.farmmit.R
import com.theelites.farmmit.databinding.ActivityTaskBinding

class TaskActivity : AppCompatActivity() {
    lateinit var binding: ActivityTaskBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@TaskActivity, R.layout.activity_task)

        binding.viewjob.setOnClickListener {
            startActivity(Intent(this@TaskActivity, MainActivity::class.java))
        }

        binding.createjob.setOnClickListener {
            startActivity(Intent(this@TaskActivity, JobCreateActivity::class.java))
        }
    }
}