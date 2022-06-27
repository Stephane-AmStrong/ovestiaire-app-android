package com.globodai.ovestiaire.data.repository

import android.net.Uri
import com.globodai.ovestiaire.data.models.*
import com.globodai.ovestiaire.data.network.ArticleApi


class ArticleRepository(
    private val articleApi: ArticleApi,
) : BaseRepository() {

//    private var _articleSelected: Article? = null
//    private var _categorySelected: String? = null
//    private var _equipmentSelected: Equipment? = null
//    private var _genderSelected: String? = null
//    private var _sportSelected: String? = null
//    private var _brandSelected: String? = null
//    private var _sizeSelected: String? = null
//    private var _colourSelected: Colour? = null
//    private var _conditionSelected: Condition? = null
//    private var _deliveryChoiceSelected: String? = null

    private var _tmpArticlePictures = mutableListOf<Uri>()

    suspend fun registerArticle(article: Article) = safeApiCall {
        return@safeApiCall if (article.id == 0) articleApi.createArticle(article) else articleApi.updateArticle(
            article.id,
            article
        )
    }

    suspend fun deleteArticle(id: Int) = safeApiCall {
        articleApi.deleteArticle(id)
    }

    suspend fun getTmpArticlePictures() = safeApiCall {
        _tmpArticlePictures
    }

    suspend fun addTmpArticlePicture(uri: Uri) = safeApiCall {
        _tmpArticlePictures.add(uri)
        _tmpArticlePictures
    }

    suspend fun removeTmpArticlePicture(uri: Uri) = safeApiCall {
        _tmpArticlePictures.remove(uri)
        _tmpArticlePictures
    }

    suspend fun getArticles() = safeApiCall {
        articleApi.getArticles()
    }

    suspend fun getArticle(id: Int) = safeApiCall {
        articleApi.getArticle(id)
    }

    suspend fun setArticle(article: Article?) = safeApiCall {
//        _articleSelected = article
        article
    }

    suspend fun getCategories() = safeApiCall {
        articleApi.getCategories()
    }

    suspend fun setCategory(category: String?) = safeApiCall {
        category
    }

    suspend fun getEquipments(
        sport: String,
        category: String,
    ) = safeApiCall {
        articleApi.getEquipments(sport, category)
    }

    suspend fun setEquipment(equipment: Equipment?) = safeApiCall {
//        _equipmentSelected = equipment
        equipment
    }

    suspend fun setGender(gender: Gender?) = safeApiCall {
        /*_genderSelected = gender
        return@safeApiCall */gender
    }


    suspend fun getSports() = safeApiCall {
        articleApi.getSports()
    }

    suspend fun setSport(sport: String?) = safeApiCall {
        sport
        //return@safeApiCall sport
    }

    suspend fun setSize1(size: Int) = safeApiCall {
        size
    }

    suspend fun setSize2(size: Int) = safeApiCall {
        size
    }

    suspend fun setSize3(size: Int) = safeApiCall {
        size
    }


    //sport
    suspend fun getBrands(
        sport: String,
    ) = safeApiCall {
        articleApi.getBrands(sport)
    }

    suspend fun setBrand(brand: String?) = safeApiCall {
        brand
    }


    //Size
    suspend fun setSize(size: String?) = safeApiCall {
        size
    }


    //Colour
    suspend fun getColours(
    ) = safeApiCall {
        articleApi.getColours()
    }

    suspend fun setColour(colour: Colour?) = safeApiCall {
        colour
    }


    //Material
    suspend fun getMaterials() = safeApiCall {
        articleApi.getMaterials()
    }

    suspend fun setMaterial(material: String?) = safeApiCall {
        material
    }


    //Condition
    suspend fun getConditions() = safeApiCall {
        articleApi.getConditions()
    }

    suspend fun setCondition(condition: Condition?) = safeApiCall {
        condition
    }


    //Delivery Choices
    suspend fun getDeliveryChoices() = safeApiCall {
        articleApi.getDeliveryChoices()
    }

    suspend fun setDeliveryChoice(deliveryChoice: String?) = safeApiCall {
        deliveryChoice
    }


    //Weight Package
    suspend fun getWeightPackages() = safeApiCall {
        articleApi.getWeightPackages()
    }

    suspend fun setWeightPackage(weightPackage: String?) = safeApiCall {
        weightPackage
    }


}