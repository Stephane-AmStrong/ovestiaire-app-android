package com.globodai.ovestiaire._core.models

import com.globodai.ovestiaire.data.models.User
import com.globodai.ovestiaire.data.network.UserApi
import com.globodai.ovestiaire.data.repository.BaseRepository

class UserRepository(
    private val userApi: UserApi,
) : BaseRepository() {

    private var _userSelected: User? = null

    suspend fun registerUser(user: User) = safeApiCall {
        return@safeApiCall if (user.id == null) userApi.createUser(user) else userApi.updateUser(
            user.id,
            user
        )
    }

    suspend fun deleteUser(id: String) = safeApiCall {
        userApi.deleteUser(id)
    }

    suspend fun getUsers() = safeApiCall {
        userApi.getUsers()
    }

    suspend fun getUser(id: String) = safeApiCall {
        userApi.getUser(id)
    }

    suspend fun setUser(user: User?) = safeApiCall {
        this._userSelected = user
        return@safeApiCall user
    }
}