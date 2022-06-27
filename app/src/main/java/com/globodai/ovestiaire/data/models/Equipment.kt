package com.globodai.ovestiaire.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Equipment(
    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("sport")
    @Expose
    var sport: String,

    @SerializedName("category")
    @Expose
    var category: String,
    @SerializedName("genders")
    @Expose
    var genders: List<Gender>,

    var isSelected: Boolean = false,
) {

    @SerializedName("material")
    @Expose
    var material: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    constructor(
        id: Int,
        name: String,
        sport: String,
        category: String,
        genders: List<Gender>,
        isSelected: Boolean = false,
        material: String?,
        createdAt: String?,
        updatedAt: String?
    ) : this(
        id,
        name,
        sport,
        category,
        genders,
        isSelected
    ) {
        this.genders = genders
        this.material = material
        this.createdAt = createdAt
        this.updatedAt = updatedAt
    }

    constructor(
        equipment: Equipment,
        isSelected: Boolean
    ):this(
        equipment.id,
        equipment.name,
        equipment.sport,
        equipment.category,
        equipment.genders,
        isSelected,
        equipment.material,
        equipment.createdAt,
        equipment.updatedAt,
    )
}