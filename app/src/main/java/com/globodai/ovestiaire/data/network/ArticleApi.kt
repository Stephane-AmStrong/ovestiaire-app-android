package com.globodai.ovestiaire.data.network

import com.globodai.ovestiaire.data.models.Article
import com.globodai.ovestiaire.data.models.Colour
import com.globodai.ovestiaire.data.models.Condition
import com.globodai.ovestiaire.data.models.Equipment
import retrofit2.http.*

interface ArticleApi {

    //Article
    @GET("articles")
    suspend fun getArticles(): List<Article>

    @GET("articles/{id}")
    suspend fun getArticle(@Path("id") id: Int): Article

    @POST("articles")
    suspend fun createArticle(@Body article: Article): Article

    @PUT("articles/{id}")
    suspend fun updateArticle(@Path("id") id:Int, @Body article: Article): Article

    @DELETE("articles/{id}")
    suspend fun deleteArticle(@Path("id") id:Int)

    //Category
    @GET("listing_category")
    suspend fun getCategories(): List<String>

    //Equipment
    @GET("equipments.json")
    suspend fun getEquipments(
        @Query("sport")
        sport: String,
        @Query("category")
        category: String,
    ): List<Equipment>

    //Sport
    @GET("listing_sports")
    suspend fun getSports(): List<String>

    //Brand
    @GET("listing_brand")
    suspend fun getBrands(
        @Query("sport")
        sport: String,
    ): List<String>

    //Color
    @GET("listing_color")
    suspend fun getColours(): List<Colour>

    //Material
    @GET("listing_material")
    suspend fun getMaterials(): List<String>

    //Condition
    @GET("listing_condition")
    suspend fun getConditions(): List<Condition>

    //Delivery Choices
    @GET("listing_delivery_choices")
    suspend fun getDeliveryChoices(): List<String>

    //Weight Package
    @GET("listing_weight_package")
    suspend fun getWeightPackages(): List<String>

}