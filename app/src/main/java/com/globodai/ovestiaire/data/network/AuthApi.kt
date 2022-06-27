package com.globodai.ovestiaire.data.network

import com.globodai.ovestiaire.data.models.User
import com.globodai.ovestiaire.data.models.dtos.LoginRequest
import com.globodai.ovestiaire.data.models.dtos.LoginResponse
import com.globodai.ovestiaire.data.models.dtos.UserRegistrationRequest
import okhttp3.ResponseBody
import retrofit2.http.*

interface AuthApi {
    //authentications
//    @GET("authentications/users/count")
//    suspend fun countUsers(): Int

    @POST("auth/user/registration")
    suspend fun registerUser(@Body userRegistrationRequest: UserRegistrationRequest): User

    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ) : LoginResponse

    @POST("logout")
    suspend fun logout(): ResponseBody
}