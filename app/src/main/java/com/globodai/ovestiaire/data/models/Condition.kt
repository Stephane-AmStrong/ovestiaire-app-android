package com.globodai.ovestiaire.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Condition(
    @SerializedName("title")
    @Expose
    var title: String,
    @SerializedName("subtitle")
    @Expose
    var subtitle: String,
    var isSelected: Boolean = false
)