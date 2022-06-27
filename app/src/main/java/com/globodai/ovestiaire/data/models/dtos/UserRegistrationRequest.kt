package com.globodai.ovestiaire.data.models.dtos

data class UserRegistrationRequest(
    val id: String?,
    val name: String,
    val firstName: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
)