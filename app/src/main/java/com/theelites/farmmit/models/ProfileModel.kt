package com.theelites.farmmit.models

data class ProfileModel(
    val email:String,
    val name: String,
    val phone:String,
    val address: String,
    val state: Int,
    val category: String,
    val profilePicture: Int
)
