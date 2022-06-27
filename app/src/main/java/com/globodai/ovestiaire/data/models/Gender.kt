package com.globodai.ovestiaire.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Gender(
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("sizes_fr")
    @Expose
    var sizes: List<String>,
    var isSelected : Boolean = false
)