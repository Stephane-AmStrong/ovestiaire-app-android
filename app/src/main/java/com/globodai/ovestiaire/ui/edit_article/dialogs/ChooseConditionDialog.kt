package com.globodai.ovestiaire.ui.edit_article.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.globodai.ovestiaire.R
import com.globodai.ovestiaire.data.models.Condition
import com.globodai.ovestiaire.data.network.ArticleApi
import com.globodai.ovestiaire.data.network.Resource
import com.globodai.ovestiaire.data.repository.ArticleRepository
import com.globodai.ovestiaire.databinding.DiablogChooseConditionBinding
import com.globodai.ovestiaire.ui.article.ArticleViewModel
import com.globodai.ovestiaire.ui.base.BaseBottomSheet
import com.globodai.ovestiaire.ui.edit_article.adapters.ConditionAdapter
import com.globodai.ovestiaire.utils.handleApiError
import com.globodai.ovestiaire.utils.toast
import com.globodai.ovestiaire.utils.visible



import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ChooseConditionDialog  : BaseBottomSheet<ArticleViewModel, DiablogChooseConditionBinding, ArticleRepository>(), ConditionAdapter.Interaction {

    private var conditions : List<Condition>? = null
    private var choosenCondition: Condition? = null
    private var conditionAdapter: ConditionAdapter? = null

    private val TAG = "ChooseConditionFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setTitle(R.string.title_choose_condition)

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_validate_choice -> {
                    validateChoise()
                    true
                }

                R.id.action_cancel -> {
                    dismiss()
                    true
                }

                else -> false
            }
        }

        initRecyclers()
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

        viewModel.conditionSelected.observe(viewLifecycleOwner) {conditionSelection ->
            binding.include.progressBar.visible(conditionSelection is Resource.Loading)
            binding.include.swipeRefreshLayout.isRefreshing = conditionSelection is Resource.Loading

            when (conditionSelection) {
                is Resource.Success -> {
                    conditionSelection.value?.let { conditionSelected ->
                        conditions?.let { lstConditions ->
                            conditionAdapter?.submitList(
                                lstConditions.map { conditionDto ->
                                    if (conditionSelected.title == conditionDto.title){
                                        Condition(conditionDto.title,conditionDto.subtitle, true)
                                    }else{
                                        Condition(conditionDto.title,conditionDto.subtitle, false)
                                    }
                                }
                            )
                        }

                        choosenCondition = conditionSelection.value
                    }
                }

                is Resource.Failure -> handleApiError(conditionSelection)

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
                    choosenCondition = conditionSelection.value
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
            conditionAdapter = ConditionAdapter(this@ChooseConditionDialog)
            adapter = conditionAdapter
        }
    }


    private fun validateChoise() {
        if (choosenCondition != null) {
            dismiss()
        }else{
            toast(resources.getString(R.string.error_condition_required))
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
    ) = DiablogChooseConditionBinding.inflate(inflater, container, false)


    override fun getFragmentRepository(): ArticleRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ArticleApi::class.java, token)
        return ArticleRepository(api)
    }

    override fun onItemSelected(position: Int, condition: Condition) {
        if (condition.title.isNotEmpty()) viewModel.setCondition(condition)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChooseConditionDialog()
    }
}