package com.theelites.farmmit.models

data class MainJobModel (
    val id: String,
    val type: String?,
    val url: String?,
    val createdAt: String? ,
    val company: String? ,
    val companyUrl: String? ,
    val location: String? ,
    val title: String? ,
    val description: String? ,
    val howToApply: String? ,
    val categoryLogo: String? ,
    var ismark : Boolean,
    val latitudee:String?,
    val longitudee:String?
){
    constructor():this("0",null,null,null,null,null,null,null,null,null,null,false,null,null)
}