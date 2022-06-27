package com.globodai.ovestiaire._core.models

import com.globodai.ovestiaire.data.models.Wallet
import com.globodai.ovestiaire.data.network.WalletApi
import com.globodai.ovestiaire.data.repository.BaseRepository

class WalletRepository(
    private val walletApi: WalletApi,
) : BaseRepository() {

    private var _walletSelected: Wallet? = null

    suspend fun getWallets() = safeApiCall {
        walletApi.getWallets()
    }

    suspend fun getWallet(id: String) = safeApiCall {
        walletApi.getWallet(id)
    }

    suspend fun setWallet(wallet: Wallet?) = safeApiCall {
        this._walletSelected = wallet
        return@safeApiCall wallet
    }
}