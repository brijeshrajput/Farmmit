package com.theelites.farmmit.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.theelites.farmmit.R
import com.theelites.farmmit.databinding.ActivityMainBinding
import com.theelites.farmmit.viewmodel.MainViewModel
import com.theelites.farmmit.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel:MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //data binding
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)

        // ViewModel
        mainViewModel = ViewModelProvider(this, MainViewModelFactory("Bunty")).get(MainViewModel::class.java)

        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this

        // observe live data change
        //mainViewModel.data.observe()
    }
}