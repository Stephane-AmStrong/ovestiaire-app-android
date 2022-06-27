package com.globodai.ovestiaire.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("equipment_id")
    @Expose
    var equipmentId: Int,
    @SerializedName("color")
    @Expose
    var color: String?,
    @SerializedName("condition")
    @Expose
    var condition: String?,
    @SerializedName("price")
    @Expose
    var price: Float,
    @SerializedName("size_gender_brand_material")
    @Expose
    var sizeGenderBrandMaterial : String
) {


    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    constructor(
        id: Int,
        equipmentId: Int,
        color: String?,
        condition: String?,
        price: Float,
        sizeGenderBrandMaterial: String,
        createdAt: String?,
        updatedAt: String?
    ) : this(
        id,
        equipmentId,
        color,
        condition,
        price,
        sizeGenderBrandMaterial,
    ){
        this.id = id
        this.equipmentId = equipmentId
        this.color = color
        this.condition = condition
        this.price = price
        this.sizeGenderBrandMaterial = sizeGenderBrandMaterial
        this.createdAt = createdAt
        this.updatedAt = updatedAt
    }
}