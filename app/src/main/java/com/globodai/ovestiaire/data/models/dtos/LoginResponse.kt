package com.globodai.ovestiaire.data.models.dtos

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class LoginResponse(
    val email: String?,
    val error: String?,
    val name: String?,
    @SerializedName("exp")
    @Expose
    val expireDate: Date,
    val token: String?,
    val telephone: String?,
    val username: String?,
)
