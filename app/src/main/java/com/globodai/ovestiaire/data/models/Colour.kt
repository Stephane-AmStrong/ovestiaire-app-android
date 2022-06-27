package com.globodai.ovestiaire.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Colour(
    @SerializedName("color")
    @Expose
    var name: String,
    @SerializedName("code_hexa")
    @Expose
    var codeHexa: String,
    var isSelected: Boolean = false
)