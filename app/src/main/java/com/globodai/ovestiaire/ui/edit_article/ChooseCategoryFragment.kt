package com.globodai.ovestiaire.ui.edit_article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.globodai.ovestiaire.data.models.dtos.CategoryDto
import com.globodai.ovestiaire.data.network.ArticleApi
import com.globodai.ovestiaire.data.network.Resource
import com.globodai.ovestiaire.data.repository.ArticleRepository
import com.globodai.ovestiaire.databinding.FragmentMultiChoiceBinding
import com.globodai.ovestiaire.ui.article.ArticleViewModel
import com.globodai.ovestiaire.ui.base.BaseFragment
import com.globodai.ovestiaire.ui.edit_article.adapters.CategoryAdapter
import com.globodai.ovestiaire.utils.handleApiError
import com.globodai.ovestiaire.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ChooseCategoryFragment :
    BaseFragment<ArticleViewModel, FragmentMultiChoiceBinding, ArticleRepository>(),SearchView.OnQueryTextListener,
    CategoryAdapter.Interaction {
    private val ARG_TITLE = "com.globodai.ovestiaire.ui.edit_article"

    private lateinit var categories: List<CategoryDto>

    private var categoryAdapter: CategoryAdapter? = null
    private var title: String? = null
    private val TAG = "ChooseCategoryFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            title = it.getString(ARG_TITLE)
        }

        initRecyclers()
        viewModel.getCategories()
        binding.include.progressBar.visible(false)
        viewModel.categories.observe(viewLifecycleOwner) {
            binding.include.progressBar.visible(it is Resource.Loading)
            binding.include.swipeRefreshLayout.isRefreshing = it is Resource.Loading

            when (it) {
                is Resource.Success -> {
                    categories = it.value.map { categoryName-> CategoryDto(categoryName, false) }
                    categoryAdapter?.submitList(categories)
                }

                is Resource.Failure -> handleApiError(it)

                else -> {}
            }
        }

        viewModel.categorySelected.observe(viewLifecycleOwner) {
            binding.include.progressBar.visible(it is Resource.Loading)
            binding.include.swipeRefreshLayout.isRefreshing = it is Resource.Loading

            when (it) {
                is Resource.Success -> {
                    it.value?.let { categoryName ->
                        categoryAdapter?.submitList(categories.map { categoryDto ->
                            if (categoryName == categoryDto.name){
                                CategoryDto(categoryDto.name, true)
                            }else{
                                CategoryDto(categoryDto.name, false)
                            }
                        })
                    }
                }

                is Resource.Failure -> handleApiError(it)

                else -> {}
            }
        }

        binding.include.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getCategories()
        }
    }

    private fun initRecyclers(){
        binding.include.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            categoryAdapter = CategoryAdapter(this@ChooseCategoryFragment)
            adapter = categoryAdapter
        }
    }

    override fun getViewModel() = ArticleViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMultiChoiceBinding.inflate(inflater, container, false)


    override fun getFragmentRepository(): ArticleRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ArticleApi::class.java, token)
        return ArticleRepository(api)
    }

    override fun onItemSelected(position: Int, category: CategoryDto) {
        viewModel.setCategory(category.name)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChooseCategoryFragment()
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            categoryAdapter?.submitList(categories.filter { it.name.contains(newText, true) || it.name.contains(newText, true) })
        } else {
            categoryAdapter?.submitList(categories)
        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        onQueryTextChange(query)
        return true
    }
}