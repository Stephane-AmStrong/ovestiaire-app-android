package com.globodai.ovestiaire.ui.edit_article



import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.globodai.ovestiaire.R
import com.globodai.ovestiaire.data.models.dtos.SportDto
import com.globodai.ovestiaire.data.network.ArticleApi
import com.globodai.ovestiaire.data.network.Resource
import com.globodai.ovestiaire.data.repository.ArticleRepository
import com.globodai.ovestiaire.databinding.FragmentChooseSportBinding
import com.globodai.ovestiaire.ui.article.ArticleViewModel
import com.globodai.ovestiaire.ui.base.BaseFragment
import com.globodai.ovestiaire.ui.edit_article.adapters.SportAdapter
import com.globodai.ovestiaire.utils.handleApiError
import com.globodai.ovestiaire.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class ChooseSportFragment :
    BaseFragment<ArticleViewModel, FragmentChooseSportBinding, ArticleRepository>(), SearchView.OnQueryTextListener, SportAdapter.Interaction {

    private val ARG_TITLE = "com.globodai.ovestiaire.ui.edit_article"

    //private lateinit var sportNames: List<String>
    private lateinit var sports : List<SportDto>
    private var sportAdapter: SportAdapter? = null
    private var title: String? = null
    private val TAG = "ChooseSportFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            title = it.getString(ARG_TITLE)
        }

        initRecyclers()
        viewModel.getSports()
        binding.include.progressBar.visible(false)
        viewModel.sports.observe(viewLifecycleOwner, Observer {
            binding.include.progressBar.visible(it is Resource.Loading)
            binding.include.swipeRefreshLayout.isRefreshing = it is Resource.Loading

            when (it) {
                is Resource.Success -> {
                    val groupedSports = arrayListOf<String>()
                    val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

                    for (letter in alphabet){
                        groupedSports.add(letter.toString())
                        groupedSports.addAll(it.value.filter { sport -> sport.first().equals(letter,true) })
                    }

                    sports = groupedSports.map { groupedSport -> SportDto(groupedSport, false) }
                    sportAdapter?.submitList(sports)
                }

                is Resource.Failure -> handleApiError(it)

                else -> {}
            }
        })

        viewModel.sportSelected.observe(viewLifecycleOwner, Observer {
            binding.include.progressBar.visible(it is Resource.Loading)
            binding.include.swipeRefreshLayout.isRefreshing = it is Resource.Loading

            when (it) {
                is Resource.Success -> {
                    it.value?.let { sportName ->
                        sportAdapter?.submitList(
                            sports.map { sportDto ->
                                if (sportName == sportDto.name){
                                    SportDto(sportDto.name,  true)
                                }else{
                                    SportDto(sportDto.name, false)
                                }
                            }
                        )

                    }
                }

                is Resource.Failure -> handleApiError(it)

                else -> {}
            }
        })

        binding.include.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getSports()
        }
    }

    private fun updateUI(sportNames: List<String>) {
        /*val alphabet ="ABCDEFGHIJKLMNOPQRSTUVWXYZ"

        sports = mutableListOf()

        for (letter in alphabet){
            sports.add(SportDto(letter.toString()))
            sports.addAll(sportNames.filter { it.first().equals(letter,true) }.map { SportDto(it) })
        }

        with(binding) {
            mAdapter = GroupAdapter<GroupieViewHolder>().apply {
                addAll(sports.toSportRow())
                setOnItemClickListener(onItemClickListener)
            }

            include.recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = mAdapter
            }
        }*/
    }

    private fun initRecyclers(){
        binding.include.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            sportAdapter = SportAdapter(this@ChooseSportFragment)
            adapter = sportAdapter
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
    ) = FragmentChooseSportBinding.inflate(inflater, container, false)

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

    override fun onItemSelected(position: Int, sport: SportDto) {
        if (sport.name.count()>1) viewModel.setSport(sport.name)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChooseSportFragment()
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null){
            sportAdapter?.submitList(sports.filter { it.name.contains(newText, true) || it.name.contains(newText, true)})
        }else{
            sportAdapter?.submitList(sports)
        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        onQueryTextChange(query)
        return true
    }
}