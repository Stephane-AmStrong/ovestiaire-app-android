package com.globodai.ovestiaire.ui.edit_article



import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.globodai.ovestiaire.R
import com.globodai.ovestiaire.data.models.Gender
import com.globodai.ovestiaire.data.network.ArticleApi
import com.globodai.ovestiaire.data.network.Resource
import com.globodai.ovestiaire.data.repository.ArticleRepository
import com.globodai.ovestiaire.databinding.FragmentMultiChoiceBinding
import com.globodai.ovestiaire.ui.article.ArticleViewModel
import com.globodai.ovestiaire.ui.base.BaseFragment
import com.globodai.ovestiaire.ui.edit_article.adapters.GenderAdapter
import com.globodai.ovestiaire.utils.handleApiError
import com.globodai.ovestiaire.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ChooseGenderFragment :
    BaseFragment<ArticleViewModel, FragmentMultiChoiceBinding, ArticleRepository>(), SearchView.OnQueryTextListener, GenderAdapter.Interaction {

    private lateinit var genders : List<Gender>
    private var genderAdapter: GenderAdapter? = null

    private val TAG = "ChooseGenderFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclers()
        //equipmentSelected
        binding.include.progressBar.visible(false)
        viewModel.equipmentSelected.observe(viewLifecycleOwner) { equipment ->
            binding.include.progressBar.visible(equipment is Resource.Loading)
            when (equipment) {
                is Resource.Success -> {
                    equipment.value.let {
                        it?.let { chosenEquipmentNonNull ->
                            genders = chosenEquipmentNonNull.genders
                            genderAdapter?.submitList(genders)

                        }
                    }
                }

                is Resource.Failure -> handleApiError(equipment)

                else -> {}
            }
        }

        viewModel.genderSelected.observe(viewLifecycleOwner) {genderSelection ->
            binding.include.progressBar.visible(genderSelection is Resource.Loading)
            binding.include.swipeRefreshLayout.isRefreshing = genderSelection is Resource.Loading

            when (genderSelection) {
                is Resource.Success -> {
                    genderSelection.value?.let {
//                        genders.map { genderDto ->
//                            genderDto.isSelected = it.name == genderDto.name
//                        }

                        genderAdapter?.submitList(
                            genders.map { genderDto ->
                                if (it.name == genderDto.name){
                                    Gender(genderDto.name, genderDto.sizes, true)
                                }else{
                                    Gender(genderDto.name, genderDto.sizes, false)
                                }
                            }
                        )
                    }

                }

                is Resource.Failure -> handleApiError(genderSelection)

                else -> {}
            }
        }

    }

    private fun initRecyclers(){
        binding.include.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            genderAdapter = GenderAdapter(this@ChooseGenderFragment)
            adapter = genderAdapter
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

    override fun onItemSelected(position: Int, gender: Gender) {
        if (gender.name.isNotEmpty()) viewModel.setGender(gender)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ChooseGenderFragment()
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null){
            genderAdapter?.submitList(genders.filter { it.name.contains(newText, true) || it.name.contains(newText, true)})
        }else{
            genderAdapter?.submitList(genders)
        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        onQueryTextChange(query)
        return true
    }
}