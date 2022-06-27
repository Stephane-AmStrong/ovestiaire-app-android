package com.globodai.ovestiaire.ui.edit_article



import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.globodai.ovestiaire.R
import com.globodai.ovestiaire.data.models.Condition
import com.globodai.ovestiaire.data.network.ArticleApi
import com.globodai.ovestiaire.data.network.Resource
import com.globodai.ovestiaire.data.repository.ArticleRepository
import com.globodai.ovestiaire.databinding.FragmentMultiChoiceBinding
import com.globodai.ovestiaire.ui.article.ArticleViewModel
import com.globodai.ovestiaire.ui.base.BaseFragment
import com.globodai.ovestiaire.ui.edit_article.adapters.ConditionAdapter
import com.globodai.ovestiaire.utils.handleApiError
import com.globodai.ovestiaire.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ChooseConditionFragment :
    BaseFragment<ArticleViewModel, FragmentMultiChoiceBinding, ArticleRepository>(), SearchView.OnQueryTextListener,ConditionAdapter.Interaction {

    private lateinit var conditions : List<Condition>
    private var conditionAdapter: ConditionAdapter? = null

    private val TAG = "ChooseConditionFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclers()
        viewModel.getConditions()
        viewModel.conditions.observe(viewLifecycleOwner) {
            binding.include.progressBar.visible(it is Resource.Loading)
            binding.include.swipeRefreshLayout.isRefreshing = it is Resource.Loading
            when (it) {
                is Resource.Success -> {
                    conditions = it.value
                    conditionAdapter?.submitList(conditions)
                }

                is Resource.Failure -> handleApiError(it)

                else -> {}
            }
        }



        binding.include.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getConditions()
        }

        viewModel.conditionSelected.observe(viewLifecycleOwner) {conditionSelection ->
            binding.include.progressBar.visible(conditionSelection is Resource.Loading)
            binding.include.swipeRefreshLayout.isRefreshing = conditionSelection is Resource.Loading

            when (conditionSelection) {
                is Resource.Success -> {
                    conditionSelection.value?.let { condition ->
                        conditions.map { conditionDto ->
                            conditionDto.isSelected = condition == conditionDto
                        }

                        conditionAdapter?.submitList(
                            conditions.map { conditionDto ->
                                if (condition.title == conditionDto.title){
                                    Condition(condition.title, conditionDto.subtitle, true)
                                }else{
                                    Condition(condition.title, conditionDto.subtitle, false)
                                }
                            }
                        )
                    }
                }

                is Resource.Failure -> handleApiError(conditionSelection)

                else -> {}
            }
        }
    }

    private fun initRecyclers(){
        binding.include.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            conditionAdapter = ConditionAdapter(this@ChooseConditionFragment)
            adapter = conditionAdapter
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

    override fun onItemSelected(position: Int, condition: Condition) {
        if (condition.title.isNotEmpty()) {
            viewModel.setCondition(condition)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ChooseConditionFragment()
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null){
            conditionAdapter?.submitList(conditions.filter { it.title.contains(newText, true) || it.subtitle.contains(newText, true)})
        }else{
            conditionAdapter?.submitList(conditions)
        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        onQueryTextChange(query)
        return true
    }
}