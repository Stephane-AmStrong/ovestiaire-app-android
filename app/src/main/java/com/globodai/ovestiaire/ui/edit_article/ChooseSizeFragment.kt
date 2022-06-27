package com.globodai.ovestiaire.ui.edit_article

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.globodai.ovestiaire.R
import com.globodai.ovestiaire.data.models.dtos.SizeDto
import com.globodai.ovestiaire.data.network.ArticleApi
import com.globodai.ovestiaire.data.network.Resource
import com.globodai.ovestiaire.data.repository.ArticleRepository
import com.globodai.ovestiaire.databinding.FragmentChooseSizeBinding
import com.globodai.ovestiaire.ui.article.ArticleViewModel
import com.globodai.ovestiaire.ui.base.BaseFragment
import com.globodai.ovestiaire.ui.edit_article.adapters.SizeAdapter
import com.globodai.ovestiaire.utils.handleApiError
import com.globodai.ovestiaire.utils.visible



import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class ChooseSizeFragment :
    BaseFragment<ArticleViewModel, FragmentChooseSizeBinding, ArticleRepository>(), SearchView.OnQueryTextListener, SizeAdapter.Interaction {

    private lateinit var sizes : List<SizeDto>
    private var sizeAdapter: SizeAdapter? = null

    private val TAG = "ChooseSizeFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclers()
        //equipmentSelected
        viewModel.genderSelected.observe(viewLifecycleOwner) { genderSelection ->
            binding.include.progressBar.visible(genderSelection is Resource.Loading)
            binding.include.swipeRefreshLayout.isRefreshing = genderSelection is Resource.Loading

            when (genderSelection) {
                is Resource.Success -> {
                    genderSelection.value?.let {chosenGenderNonNull ->
                        updateUI(chosenGenderNonNull.sizes.map { genderName-> SizeDto(genderName, false) })
                    }
                }

                is Resource.Failure -> handleApiError(genderSelection)

                else -> {}
            }
        }

        viewModel.sizeSelected.observe(viewLifecycleOwner) {sizeSelection ->
            binding.include.progressBar.visible(sizeSelection is Resource.Loading)
            binding.include.swipeRefreshLayout.isRefreshing = sizeSelection is Resource.Loading

            when (sizeSelection) {
                is Resource.Success -> {
                    sizeSelection.value?.let { sizeName ->
                        sizeAdapter?.submitList(
                            sizes.map { sizeDto ->
                                if (sizeName == sizeDto.name){
                                    SizeDto(sizeDto.name, true)
                                }else{
                                    SizeDto(sizeDto.name, false)
                                }
                            }
                        )

                    }
                }

                is Resource.Failure -> handleApiError(sizeSelection)

                else -> {}
            }
        }

        binding.txtSize1.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                viewModel.editSize1(if (s.toString().isNotEmpty()) s.toString().toInt() else 0 )
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

        binding.txtSize2.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                viewModel.editSize2(if (s.toString().isNotEmpty()) s.toString().toInt() else 0 )
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

        binding.txtSize3.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                viewModel.editSize3(if (s.toString().isNotEmpty()) s.toString().toInt() else 0 )
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

    }

    private fun initRecyclers(){
        binding.include.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            sizeAdapter = SizeAdapter(this@ChooseSizeFragment)
            adapter = sizeAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.include.recyclerView.adapter = null
    }

    private fun updateUI(sizes: List<SizeDto>) {
        this.sizes = sizes

        if(!sizes.any()){
            this.sizes = listOf(SizeDto("NA",true))
        }else{
            with(binding){
                include.recyclerView.visible(!sizes.any { it.name == "cm" })
                controllerContainer.visible(sizes.any { it.name == "cm" })

                lblSize1.visible(sizes.count { it.name == "cm" }>=1)
                lblSize2.visible(sizes.count { it.name == "cm" }>=2)
                lblSize3.visible(sizes.count { it.name == "cm" }>=3)
            }
        }
        sizeAdapter?.submitList(this.sizes)
    }

    override fun getViewModel() = ArticleViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentChooseSizeBinding.inflate(inflater, container, false)

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

    override fun onItemSelected(position: Int, size: SizeDto) {
        if (size.name.isNotEmpty()) viewModel.setSize(size.name)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ChooseSizeFragment()
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null){
            sizeAdapter?.submitList(sizes.filter { it.name.contains(newText, true) || it.name.contains(newText, true)})
        }else{
            sizeAdapter?.submitList(sizes)
        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        onQueryTextChange(query)
        return true
    }
}