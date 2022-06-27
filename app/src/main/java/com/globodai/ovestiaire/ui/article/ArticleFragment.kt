package com.globodai.ovestiaire.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.globodai.ovestiaire.data.models.Article
import com.globodai.ovestiaire.data.network.ArticleApi
import com.globodai.ovestiaire.data.repository.ArticleRepository
import com.globodai.ovestiaire.databinding.FragmentArticleBinding
import com.globodai.ovestiaire.ui.base.BaseFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ArticleFragment : BaseFragment<ArticleViewModel, FragmentArticleBinding, ArticleRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getArticles()
    }

    private fun updateUI(articles: List<Article>) {


    }

    override fun getViewModel() = ArticleViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentArticleBinding.inflate(inflater, container, false)


    override fun getFragmentRepository(): ArticleRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ArticleApi::class.java, token)
        return ArticleRepository(api)
    }


}