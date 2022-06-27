package com.globodai.ovestiaire.ui.edit_article

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.globodai.ovestiaire.R
import com.globodai.ovestiaire.data.models.Colour
import com.globodai.ovestiaire.data.network.ArticleApi
import com.globodai.ovestiaire.data.network.Resource
import com.globodai.ovestiaire.data.repository.ArticleRepository
import com.globodai.ovestiaire.databinding.FragmentMultiChoiceBinding
import com.globodai.ovestiaire.ui.article.ArticleViewModel
import com.globodai.ovestiaire.ui.base.BaseFragment
import com.globodai.ovestiaire.ui.edit_article.adapters.ColourAdapter
import com.globodai.ovestiaire.utils.handleApiError
import com.globodai.ovestiaire.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ChooseColourFragment :
    BaseFragment<ArticleViewModel, FragmentMultiChoiceBinding, ArticleRepository>(), SearchView.OnQueryTextListener,
    ColourAdapter.Interaction {

    private lateinit var colours : List<Colour>
    private var colourAdapter: ColourAdapter? = null

    private val TAG = "ChooseColourFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclers()
        viewModel.getColours()
        viewModel.colours.observe(viewLifecycleOwner) {
            binding.include.progressBar.visible(it is Resource.Loading)
            binding.include.swipeRefreshLayout.isRefreshing = it is Resource.Loading
            when (it) {
                is Resource.Success -> {
                    colours = it.value
                    colourAdapter?.submitList(colours)
                }

                is Resource.Failure -> handleApiError(it)

                else -> {}
            }
        }

        binding.include.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getColours()
        }

        viewModel.colourSelected.observe(viewLifecycleOwner) {colourSelection ->
            binding.include.progressBar.visible(colourSelection is Resource.Loading)
            binding.include.swipeRefreshLayout.isRefreshing = colourSelection is Resource.Loading

            when (colourSelection) {
                is Resource.Success -> {
                    colourSelection.value?.let {
                        colourAdapter?.submitList(
                            colours.map { colourDto ->
                                if (it.name == colourDto.name){
                                    Colour(colourDto.name, colourDto.codeHexa, true)
                                }else{
                                    Colour(colourDto.name, colourDto.codeHexa, false)
                                }
                            }
                        )
                    }
                }

                is Resource.Failure -> handleApiError(colourSelection)

                else -> {}
            }
        }
    }

    private fun initRecyclers(){
        binding.include.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            colourAdapter = ColourAdapter(this@ChooseColourFragment)
            adapter = colourAdapter
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

    override fun onItemSelected(position: Int, colour: Colour) {
        viewModel.setColour(colour)
    }

    companion object {
        @JvmStatic
        fun newInstance() =ChooseColourFragment()
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null){
            colourAdapter?.submitList(colours.filter { it.name.contains(newText, true) || it.name.contains(newText, true)})
        }else{
            colourAdapter?.submitList(colours)
        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        onQueryTextChange(query)
        return true
    }
}