package com.globodai.ovestiaire.data.network

import com.globodai.ovestiaire.data.models.Wallet
import retrofit2.http.*

interface WalletApi {
    @GET("wallets")
    suspend fun getWallets(): List<Wallet>

    @GET("wallets/{id}")
    suspend fun getWallet(@Path("id") id: String): Wallet
}