package com.theelites.farmmit.models

import android.net.Uri

data class CreateCategoryModel(
    val categoryName: String?,
    var categoryImage: String?
){
    constructor():this(null,null)
}
