package com.theelites.farmmit.viewmodel

import androidx.lifecycle.ViewModel

class MainViewModel(val data: String) : ViewModel() {
    // data present till activity destroy completely

    // data and fun - but not views
}