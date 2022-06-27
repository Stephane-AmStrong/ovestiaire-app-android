package com.globodai.ovestiaire.ui.article

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.globodai.ovestiaire.data.models.*
import com.globodai.ovestiaire.data.network.Resource
import com.globodai.ovestiaire.data.repository.ArticleRepository
import com.globodai.ovestiaire.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val articleRepository: ArticleRepository,
) : BaseViewModel(articleRepository) {

    private val _tmpArticlePictures: MutableLiveData<Resource<MutableList<Uri>>> = MutableLiveData()

    private val _article: MutableLiveData<Resource<Article>> = MutableLiveData()
    private val _articles: MutableLiveData<Resource<List<Article>>> = MutableLiveData()
    private val _articleSelected: MutableLiveData<Resource<Article?>?> = MutableLiveData()
    private val _articleRegistered: MutableLiveData<Resource<Article?>?> = MutableLiveData()

    private val _categories: MutableLiveData<Resource<List<String>>> = MutableLiveData()
    private val _categorySelected: MutableLiveData<Resource<String?>?> = MutableLiveData()

    private val _equipments: MutableLiveData<Resource<List<Equipment>>> = MutableLiveData()
    private val _equipmentSelected: MutableLiveData<Resource<Equipment?>?> = MutableLiveData()

    private val _genderSelected: MutableLiveData<Resource<Gender?>?> = MutableLiveData()
    private val _size1Edited: MutableLiveData<Resource<Int>?> = MutableLiveData()
    private val _size2Edited: MutableLiveData<Resource<Int>?> = MutableLiveData()
    private val _size3Edited: MutableLiveData<Resource<Int>?> = MutableLiveData()

    private val _sports: MutableLiveData<Resource<List<String>>> = MutableLiveData()
    private val _sportSelected: MutableLiveData<Resource<String?>?> = MutableLiveData()

    private var _brands: MutableLiveData<Resource<List<String>>> = MutableLiveData()
    private var _brandSelected: MutableLiveData<Resource<String?>?> = MutableLiveData()

    private var _sizeSelected: MutableLiveData<Resource<String?>?> = MutableLiveData()

    private var _colours: MutableLiveData<Resource<List<Colour>>> = MutableLiveData()
    private var _colourSelected: MutableLiveData<Resource<Colour?>?> = MutableLiveData()

    private var _materials: MutableLiveData<Resource<List<String>>> = MutableLiveData()
    private var _materialSelected: MutableLiveData<Resource<String?>?> = MutableLiveData()

    private var _conditions: MutableLiveData<Resource<List<Condition>>> = MutableLiveData()
    private var _conditionSelected: MutableLiveData<Resource<Condition?>?> = MutableLiveData()

    private var _deliveryChoices: MutableLiveData<Resource<List<String>>> = MutableLiveData()
    private var _deliveryChoiceSelected: MutableLiveData<Resource<String?>?> = MutableLiveData()

    private var _weightPackages: MutableLiveData<Resource<List<String>>> = MutableLiveData()
    private var _weightPackageSelected: MutableLiveData<Resource<String?>?> = MutableLiveData()


    /*

    */


    //getters & setter articles

    val tmpArticlePictures: LiveData<Resource<MutableList<Uri>>>
        get() = _tmpArticlePictures

    val article: LiveData<Resource<Article>>
        get() = _article

    val articles: LiveData<Resource<List<Article>>>
        get() = _articles

    val articleSelected: LiveData<Resource<Article?>?>
        get() = _articleSelected

    val articleRegistered: LiveData<Resource<Article?>?>
        get() = _articleRegistered


    //getters & setter Categories
    val categories: LiveData<Resource<List<String>>>
        get() = _categories

    val categorySelected: LiveData<Resource<String?>?>
        get() = _categorySelected


    //getters & setter Equipments
    val equipments: LiveData<Resource<List<Equipment>>>
        get() = _equipments

    val equipmentSelected: LiveData<Resource<Equipment?>?>
        get() = _equipmentSelected



    //getters & setter Equipments
    val genderSelected: LiveData<Resource<Gender?>?>
        get() = _genderSelected



    //getters & setter Sizes
    val size1Edited: LiveData<Resource<Int>?>
        get() = _size1Edited

    val size2Edited: LiveData<Resource<Int>?>
        get() = _size2Edited

    val size3Edited: LiveData<Resource<Int>?>
        get() = _size3Edited


    //getters & setter Sports
    val sports: LiveData<Resource<List<String>>>
        get() = _sports

    val sportSelected: LiveData<Resource<String?>?>
        get() = _sportSelected


    //getters & setter Brands
    val brands: LiveData<Resource<List<String>>>
        get() = _brands

    val brandSelected: LiveData<Resource<String?>?>
        get() = _brandSelected


    //getters & setter Sizes
    val sizeSelected: LiveData<Resource<String?>?>
        get() = _sizeSelected


    //getters & setter Colours
    val colours: LiveData<Resource<List<Colour>>>
        get() = _colours

    val colourSelected: LiveData<Resource<Colour?>?>
        get() = _colourSelected


    //getters & setter Materials
    val materials: LiveData<Resource<List<String>>>
        get() = _materials

    val materialSelected: LiveData<Resource<String?>?>
        get() = _materialSelected


    //getters & setter Conditions
    val conditions: LiveData<Resource<List<Condition>>>
        get() = _conditions

    val conditionSelected: LiveData<Resource<Condition?>?>
        get() = _conditionSelected


    val deliveryChoices: LiveData<Resource<List<String>>>
        get() = _deliveryChoices

    val deliveryChoiceSelected: LiveData<Resource<String?>?>
        get() = _deliveryChoiceSelected


    val weightPackages: LiveData<Resource<List<String>>>
        get() = _weightPackages

    val weightPackageSelected: LiveData<Resource<String?>?>
        get() = _weightPackageSelected



    fun getTmpArticlePictures() = viewModelScope.launch {
        _tmpArticlePictures.value = Resource.Loading
        _tmpArticlePictures.value = articleRepository.getTmpArticlePictures()
    }

    fun addTmpArticlePicture(uri: Uri) = viewModelScope.launch {
        _tmpArticlePictures.value = Resource.Loading
        _tmpArticlePictures.value = articleRepository.addTmpArticlePicture(uri)
        _tmpArticlePictures.value = articleRepository.getTmpArticlePictures()
    }

    fun removeTmpArticlePicture(uri: Uri) = viewModelScope.launch {
        _tmpArticlePictures.value = Resource.Loading
        _tmpArticlePictures.value = articleRepository.removeTmpArticlePicture(uri)
        _tmpArticlePictures.value = articleRepository.getTmpArticlePictures()
    }

    fun setArticle(article: Article?) = viewModelScope.launch {
        _articleSelected.value = Resource.Loading
        _articleSelected.value = articleRepository.setArticle(article)
    }

    fun getArticle(id: Int) = viewModelScope.launch {
        _article.value = Resource.Loading
        _article.value = articleRepository.getArticle(id)
    }

    fun getArticles() = viewModelScope.launch {
        _articles.value = Resource.Loading
        _articles.value = articleRepository.getArticles()
    }

    fun registerArticle(article: Article) = viewModelScope.launch {
        _articleRegistered.value = Resource.Loading
        _articleRegistered.value = articleRepository.registerArticle(article)
        _articles.value = articleRepository.getArticles()
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        _articles.value = Resource.Loading
        articleRepository.deleteArticle(article.id)
        _articles.value = articleRepository.getArticles()
    }

    fun releaseArticle() = viewModelScope.launch {
        _articleRegistered.value = null
        _articleSelected.value = null
    }


    //Categories
    fun setCategory(category: String?) = viewModelScope.launch {
        _categorySelected.value = Resource.Loading
        _categorySelected.value = articleRepository.setCategory(category)
    }

    fun getCategories() = viewModelScope.launch {
        _categories.value = Resource.Loading
        _categories.value = articleRepository.getCategories()
    }

    fun releaseCategory() = viewModelScope.launch {
        _categorySelected.value = null
    }


    //Equipments
    fun setEquipment(
        equipment: Equipment,
    ) = viewModelScope.launch {
        _equipmentSelected.value = Resource.Loading
        _equipmentSelected.value = articleRepository.setEquipment(equipment)
    }

    fun getEquipments(
        sport: String,
        category: String,
    ) = viewModelScope.launch {
        _equipments.value = Resource.Loading
        _equipments.value = articleRepository.getEquipments(sport, category)
    }

    fun releaseEquipment() = viewModelScope.launch {
        _categorySelected.value = null
    }


    //Genders
    fun setGender(gender: Gender) = viewModelScope.launch {
        _genderSelected.value = Resource.Loading
        _genderSelected.value = articleRepository.setGender(gender)
    }

    fun releaseGender() = viewModelScope.launch {
        _genderSelected.value = null
    }


    //Size1s
    fun editSize1(size: Int) = viewModelScope.launch {
        _size1Edited.value = Resource.Loading
        _size1Edited.value = articleRepository.setSize1(size)
    }

    fun releaseSize1() = viewModelScope.launch {
        _size1Edited.value = null
    }


    //Size2s
    fun editSize2(size: Int) = viewModelScope.launch {
        _size2Edited.value = Resource.Loading
        _size2Edited.value = articleRepository.setSize2(size)
    }

    fun releaseSize2() = viewModelScope.launch {
        _size2Edited.value = null
    }


    //Size3s
    fun editSize3(size: Int) = viewModelScope.launch {
        _size3Edited.value = Resource.Loading
        _size3Edited.value = articleRepository.setSize3(size)
    }

    fun releaseSize3() = viewModelScope.launch {
        _size3Edited.value = null
    }


    //Sports
    fun setSport(sport: String) = viewModelScope.launch {
        _sportSelected.value = Resource.Loading
        _sportSelected.value = articleRepository.setSport(sport)
    }

    fun getSports() = viewModelScope.launch {
        _sports.value = Resource.Loading
        _sports.value = articleRepository.getSports()
    }

    fun releaseSport() = viewModelScope.launch {
        _sportSelected.value = null
    }


    //String
    fun setBrand(brand: String) = viewModelScope.launch {
        _brandSelected.value = Resource.Loading
        _brandSelected.value = articleRepository.setBrand(brand)
    }

    fun getBrands(
        sport: String,
    ) = viewModelScope.launch {
        _brands.value = Resource.Loading
        _brands.value = articleRepository.getBrands(sport)
    }

    fun releaseBrand() = viewModelScope.launch {
        _brandSelected.value = null
    }


    //Size
    fun setSize(size: String) = viewModelScope.launch {
        _sizeSelected.value = Resource.Loading
        _sizeSelected.value = articleRepository.setSize(size)
    }

    fun releaseSize() = viewModelScope.launch {
        _sizeSelected.value = null
    }


    //Colour
    fun setColour(colour: Colour) = viewModelScope.launch {
        _colourSelected.value = Resource.Loading
        _colourSelected.value = articleRepository.setColour(colour)
    }

    fun getColours(
    ) = viewModelScope.launch {
        _colours.value = Resource.Loading
        _colours.value = articleRepository.getColours()
    }

    fun releaseColour() = viewModelScope.launch {
        _colourSelected.value = null
    }

    //Material
    fun setMaterial(material:String) = viewModelScope.launch {
        _materialSelected.value = Resource.Loading
        _materialSelected.value = articleRepository.setMaterial(material)
    }

    fun getMaterials() = viewModelScope.launch {
        _materials.value = Resource.Loading
        _materials.value = articleRepository.getMaterials()
    }

    fun releaseMaterial() = viewModelScope.launch {
        _materialSelected.value = null
    }


    //Condition
    fun setCondition(condition: Condition) = viewModelScope.launch {
        _conditionSelected.value = Resource.Loading
        _conditionSelected.value = articleRepository.setCondition(condition)
    }

    fun getConditions() = viewModelScope.launch {
        _conditions.value = Resource.Loading
        _conditions.value = articleRepository.getConditions()
    }

    fun releaseCondition() = viewModelScope.launch {
        _conditionSelected.value = null
    }

    //DeliveryChoice
    fun setDeliveryChoice(deliveryChoice: String) = viewModelScope.launch {
        _deliveryChoiceSelected.value = Resource.Loading
        _deliveryChoiceSelected.value = articleRepository.setDeliveryChoice(deliveryChoice)
    }

    fun getDeliveryChoices() = viewModelScope.launch {
        _deliveryChoices.value = Resource.Loading
        _deliveryChoices.value = articleRepository.getDeliveryChoices()
    }

    fun releaseDeliveryChoice() = viewModelScope.launch {
        _deliveryChoiceSelected.value = null
    }


    //WeightPackage
    fun setWeightPackage(weightPackage: String) = viewModelScope.launch {
        _weightPackageSelected.value = Resource.Loading
        _weightPackageSelected.value = articleRepository.setWeightPackage(weightPackage)
    }

    fun getWeightPackages() = viewModelScope.launch {
        _weightPackages.value = Resource.Loading
        _weightPackages.value = articleRepository.getWeightPackages()
    }

    fun releaseWeightPackage() = viewModelScope.launch {
        _weightPackageSelected.value = null
    }



    /*

    */

}