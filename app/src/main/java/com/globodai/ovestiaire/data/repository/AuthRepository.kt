package com.globodai.ovestiaire.data.repository

import com.globodai.ovestiaire.data.db.UserPreferences
import com.globodai.ovestiaire.data.models.User
import com.globodai.ovestiaire.data.models.dtos.LoginRequest
import com.globodai.ovestiaire.data.models.dtos.LoginResponse
import com.globodai.ovestiaire.data.models.dtos.UserRegistrationRequest
import com.globodai.ovestiaire.data.network.AuthApi

class AuthRepository(
    private val authApi: AuthApi,
    private val preferences: UserPreferences
) : BaseRepository() {
    private var _userSelected: User? = null

    suspend fun login(
        loginRequest: LoginRequest
    ) = safeApiCall {
        authApi.login(loginRequest)
    }

    suspend fun registerUser(
        adminRegistrationRequest: UserRegistrationRequest
    ) = safeApiCall {
        return@safeApiCall authApi.registerUser(adminRegistrationRequest)
    }

    suspend fun clearEverything() {
        preferences.clearEverything()
    }

    suspend fun saveAuthData(loginResponse: LoginResponse) {
        preferences.saveAuthData(loginResponse)
    }

    suspend fun setUser(user: User?) = safeApiCall {
        this._userSelected = user
        return@safeApiCall user
    }

}