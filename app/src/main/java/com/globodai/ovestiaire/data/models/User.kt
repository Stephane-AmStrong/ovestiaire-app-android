package com.globodai.ovestiaire.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("email")
    @Expose
    var email: String,

    @SerializedName("telephone")
    @Expose
    var telephone: String?
) {

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("avatar_url")
    @Expose
    var avatarUrl: String? = null

    @SerializedName("birthday")
    @Expose
    var birthday: String? = null

    constructor(
        name: String,
        username: String?,
        email: String,
        telephone: String?,
        birthday: String?
    ) : this(
        name,
        email,
        telephone
    ) {
        this.name = name
        this.username = username
        this.telephone = telephone
        this.birthday = birthday
    }

    constructor(
        id: String?,
        name: String,
        username: String?,
        email: String,
        telephone: String?,
        avatarUrl: String?,
        birthday: String?,
    ) : this(
        name,
        username,
        email,
        telephone,
        birthday
    ) {
        this.id = id
        this.avatarUrl = avatarUrl
    }
}