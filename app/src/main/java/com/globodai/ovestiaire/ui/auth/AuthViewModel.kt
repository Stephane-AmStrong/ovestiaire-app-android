package com.globodai.ovestiaire.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.globodai.ovestiaire.data.models.User
import com.globodai.ovestiaire.data.models.dtos.LoginRequest
import com.globodai.ovestiaire.data.models.dtos.LoginResponse
import com.globodai.ovestiaire.data.models.dtos.UserRegistrationRequest
import com.globodai.ovestiaire.data.network.Resource
import com.globodai.ovestiaire.data.repository.AuthRepository
import com.globodai.ovestiaire.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository,
) : BaseViewModel(repository) {

    private val _loggedIn: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    private val _userRegistered: MutableLiveData<Resource<User>> = MutableLiveData()


    val userRegistration: LiveData<Resource<User>>
        get() = _userRegistered

    val loggedIn: LiveData<Resource<LoginResponse>>
        get() = _loggedIn



    fun login(
        loginRequest: LoginRequest
    ) = viewModelScope.launch {
        _loggedIn.value = Resource.Loading
        _loggedIn.value = repository.login(loginRequest)
    }

    fun registerUser(
        userRegistrationRequest: UserRegistrationRequest
    ) = viewModelScope.launch {
        _userRegistered.value = Resource.Loading
        _userRegistered.value = repository.registerUser(userRegistrationRequest)
    }

    suspend fun saveAuthData(loginResponse: LoginResponse) {
        repository.saveAuthData(loginResponse)
    }

    suspend fun clearPreferences() {
        repository.clearEverything()
    }
}