package com.globodai.ovestiaire.data.network

import com.globodai.ovestiaire.data.models.User
import retrofit2.http.*

interface UserApi {
    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: String?): User

    @POST("users")
    suspend fun createUser(@Body user: User): User

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id:String?, @Body user: User): User

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id:String?)
}