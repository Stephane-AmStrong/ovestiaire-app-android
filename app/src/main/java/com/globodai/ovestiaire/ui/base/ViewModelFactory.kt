package com.globodai.ovestiaire.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.globodai.ovestiaire.data.repository.ArticleRepository
import com.globodai.ovestiaire.data.repository.AuthRepository
import com.globodai.ovestiaire.data.repository.BaseRepository
import com.globodai.ovestiaire.ui.article.ArticleViewModel
import com.globodai.ovestiaire.ui.auth.AuthViewModel

class ViewModelFactory(
    private val repository: BaseRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(ArticleViewModel::class.java) -> ArticleViewModel(repository as ArticleRepository) as T
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}