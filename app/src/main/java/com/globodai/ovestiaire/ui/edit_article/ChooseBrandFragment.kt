package com.globodai.ovestiaire.ui.edit_article

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.globodai.ovestiaire.R
import com.globodai.ovestiaire.data.models.dtos.BrandDto
import com.globodai.ovestiaire.data.network.ArticleApi
import com.globodai.ovestiaire.data.network.Resource
import com.globodai.ovestiaire.data.repository.ArticleRepository
import com.globodai.ovestiaire.databinding.FragmentMultiChoiceBinding
import com.globodai.ovestiaire.ui.article.ArticleViewModel
import com.globodai.ovestiaire.ui.base.BaseFragment
import com.globodai.ovestiaire.ui.edit_article.adapters.BrandAdapter
import com.globodai.ovestiaire.utils.handleApiError
import com.globodai.ovestiaire.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ChooseBrandFragment :
    BaseFragment<ArticleViewModel, FragmentMultiChoiceBinding, ArticleRepository>(), SearchView.OnQueryTextListener,
    BrandAdapter.Interaction  {

    private lateinit var brands : List<BrandDto>
    private var brandAdapter: BrandAdapter? = null

    private val TAG = "ChooseBrandFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //brandSelected
        initRecyclers()
        binding.include.progressBar.visible(false)
        viewModel.sportSelected.observe(viewLifecycleOwner) { sportSelection ->
            binding.include.progressBar.visible(sportSelection is Resource.Loading)
            binding.include.swipeRefreshLayout.isRefreshing = sportSelection is Resource.Loading

            when (sportSelection) {
                is Resource.Success -> {
                    sportSelection.value.let {
                        it?.let { chosenSportNonNull ->
                            viewModel.getBrands(chosenSportNonNull)
                        }
                    }
                }

                is Resource.Failure -> handleApiError(sportSelection)

                else -> {}
            }
        }

        viewModel.brands.observe(viewLifecycleOwner) {
            binding.include.progressBar.visible(it is Resource.Loading)
            binding.include.swipeRefreshLayout.isRefreshing = it is Resource.Loading

            when (it) {
                is Resource.Success -> {
                    brands = it.value.map { sportName-> BrandDto(sportName, false) }
                    brandAdapter?.submitList(brands)
                }

                is Resource.Failure -> handleApiError(it)

                else -> {}
            }
        }

        viewModel.brandSelected.observe(viewLifecycleOwner) {brandSelection ->
            binding.include.progressBar.visible(brandSelection is Resource.Loading)
            binding.include.swipeRefreshLayout.isRefreshing = brandSelection is Resource.Loading

            when (brandSelection) {
                is Resource.Success -> {
                    brandSelection.value?.let { brandName ->
                        brandAdapter?.submitList(
                            brands.map { brandDto ->
                                if (brandDto.name == brandDto.name){
                                    BrandDto(brandDto.name, true)
                                }else{
                                    BrandDto(brandDto.name, false)
                                }
                            }
                        )
                        
                    }

                }

                is Resource.Failure -> handleApiError(brandSelection)

                else -> {}
            }
        }


    }

    private fun initRecyclers(){
        binding.include.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            brandAdapter = BrandAdapter(this@ChooseBrandFragment)
            adapter = brandAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.include.recyclerView.adapter = null
    }


    override fun getViewModel() = ArticleViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMultiChoiceBinding.inflate(inflater, container, false)

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.with_search, menu)
        val search = menu.findItem(R.id.action_search)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }


    override fun getFragmentRepository(): ArticleRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ArticleApi::class.java, token)
        return ArticleRepository(api)
    }

    override fun onItemSelected(position: Int, brand: BrandDto) {
        viewModel.setBrand(brand.name)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ChooseBrandFragment()
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null){
            brandAdapter?.submitList(brands.filter { it.name.contains(newText, true) || it.name.contains(newText, true)})
        }else{
            brandAdapter?.submitList(brands)
        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        onQueryTextChange(query)
        return true
    }
}