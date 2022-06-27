package com.globodai.ovestiaire.ui.edit_article

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.globodai.ovestiaire.R
import com.globodai.ovestiaire.data.models.dtos.MaterialDto
import com.globodai.ovestiaire.data.network.ArticleApi
import com.globodai.ovestiaire.data.network.Resource
import com.globodai.ovestiaire.data.repository.ArticleRepository
import com.globodai.ovestiaire.databinding.FragmentMultiChoiceBinding
import com.globodai.ovestiaire.ui.article.ArticleViewModel
import com.globodai.ovestiaire.ui.base.BaseFragment
import com.globodai.ovestiaire.ui.edit_article.adapters.MaterialAdapter
import com.globodai.ovestiaire.utils.handleApiError
import com.globodai.ovestiaire.utils.visible



import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ChooseMaterialFragment :
    BaseFragment<ArticleViewModel, FragmentMultiChoiceBinding, ArticleRepository>(), SearchView.OnQueryTextListener, MaterialAdapter.Interaction {

    private lateinit var materials : List<MaterialDto>
    private var materialAdapter: MaterialAdapter? = null

    private val TAG = "ChooseMaterialFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclers()
        viewModel.getMaterials()
        viewModel.materials.observe(viewLifecycleOwner) {
            binding.include.progressBar.visible(it is Resource.Loading)
            binding.include.swipeRefreshLayout.isRefreshing = it is Resource.Loading
            when (it) {
                is Resource.Success -> {
                    materials = it.value.map { materialName-> MaterialDto(materialName, false) }
                    materialAdapter?.submitList(materials)
                }

                is Resource.Failure -> handleApiError(it)

                else -> {}
            }
        }



        binding.include.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getMaterials()
        }

        viewModel.materialSelected.observe(viewLifecycleOwner) {materialSelection ->
            binding.include.progressBar.visible(materialSelection is Resource.Loading)
            binding.include.swipeRefreshLayout.isRefreshing = materialSelection is Resource.Loading

            when (materialSelection) {
                is Resource.Success -> {
                    materialSelection.value?.let { materialName ->
                        materials.map { materialDto ->
                            materialDto.isSelected = materialName == materialDto.name
                        }

                        materialAdapter?.submitList(
                            materials.map { materialDto ->
                                if (materialName == materialDto.name){
                                    MaterialDto(materialDto.name,true)
                                }else{
                                    MaterialDto(materialDto.name,false)
                                }
                            }
                        )
                    }
                }

                is Resource.Failure -> handleApiError(materialSelection)

                else -> {}
            }
        }
    }

    private fun initRecyclers(){
        binding.include.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            materialAdapter = MaterialAdapter(this@ChooseMaterialFragment)
            adapter = materialAdapter
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

    override fun onItemSelected(position: Int, material: MaterialDto) {
        if (material.name.isNotEmpty()) viewModel.setMaterial(material.name)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChooseMaterialFragment()
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null){
            materialAdapter?.submitList(materials.filter { it.name.contains(newText, true) || it.name.contains(newText, true)})
        }else{
            materialAdapter?.submitList(materials)
        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        onQueryTextChange(query)
        return true
    }
}