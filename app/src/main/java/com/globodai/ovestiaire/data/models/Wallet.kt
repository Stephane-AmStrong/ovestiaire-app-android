package com.globodai.ovestiaire.data.models

data class Wallet(
    var month: String,
    var details: MutableList<Detail>
)