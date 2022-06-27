package com.globodai.ovestiaire.ui.edit_article



import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.globodai.ovestiaire.R
import com.globodai.ovestiaire.data.models.Equipment
import com.globodai.ovestiaire.data.network.ArticleApi
import com.globodai.ovestiaire.data.network.Resource
import com.globodai.ovestiaire.data.repository.ArticleRepository
import com.globodai.ovestiaire.databinding.FragmentMultiChoiceBinding
import com.globodai.ovestiaire.ui.article.ArticleViewModel
import com.globodai.ovestiaire.ui.base.BaseFragment
import com.globodai.ovestiaire.ui.edit_article.adapters.EquipmentAdapter
import com.globodai.ovestiaire.utils.handleApiError
import com.globodai.ovestiaire.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ChooseEquipmentFragment :
    BaseFragment<ArticleViewModel, FragmentMultiChoiceBinding, ArticleRepository>(), SearchView.OnQueryTextListener, EquipmentAdapter.Interaction {

    lateinit var equipments : List<Equipment>
    private var equipmentAdapter: EquipmentAdapter? = null
    private var sport: String? = null
    private var category: String? = null

    private val TAG = "ChooseEquipmentFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclers()
        binding.include.progressBar.visible(false)
        viewModel.sportSelected.observe(viewLifecycleOwner, Observer {
            binding.include.progressBar.visible(it is Resource.Loading)
            binding.include.swipeRefreshLayout.isRefreshing = it is Resource.Loading

            when (it) {
                is Resource.Success -> {
                    it.value.let { chosenSport ->
                        sport = chosenSport
                    }

                    sport?.let { chosenSport ->
                        category?.let { chosenCategory ->
                            viewModel.getEquipments(chosenSport,chosenCategory)
                        }
                    }
                }

                is Resource.Failure -> handleApiError(it)

                else -> {}
            }
        })

        binding.include.progressBar.visible(false)
        viewModel.categorySelected.observe(viewLifecycleOwner, Observer {
            binding.include.progressBar.visible(it is Resource.Loading)
            binding.include.swipeRefreshLayout.isRefreshing = it is Resource.Loading

            when (it) {
                is Resource.Success -> {
                    it.value.let { chosenCategory ->
                        category = chosenCategory
                    }

                    category?.let { chosenCategory ->
                        sport?.let { chosenSport ->
                            viewModel.getEquipments(chosenSport,chosenCategory)
                        }
                    }
                }

                is Resource.Failure -> handleApiError(it)

                else -> {}
            }
        })

        binding.include.progressBar.visible(false)
        viewModel.equipments.observe(viewLifecycleOwner, Observer {
            binding.include.progressBar.visible(it is Resource.Loading)
            binding.include.swipeRefreshLayout.isRefreshing = it is Resource.Loading

            when (it) {
                is Resource.Success -> {

                    val groupedEquipments = arrayListOf<Equipment>()
                    val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

                    for (letter in alphabet){
                        groupedEquipments.add(Equipment(0,letter.toString(),"","", listOf()))
                        groupedEquipments.addAll(it.value.filter { equipment -> equipment.name.first() == letter })
                    }

                    equipments = groupedEquipments.map { equipment -> Equipment(equipment.id, equipment.name, equipment.sport, equipment.category, equipment.genders, false, equipment.material, equipment.createdAt, equipment.updatedAt) }
                    
                    equipmentAdapter?.submitList(equipments)

                }

                is Resource.Failure -> handleApiError(it)

                else -> {}
            }
        })

        viewModel.equipmentSelected.observe(viewLifecycleOwner, Observer { it ->
            binding.include.progressBar.visible(it is Resource.Loading)
            binding.include.swipeRefreshLayout.isRefreshing = it is Resource.Loading

            when (it) {
                is Resource.Success -> {
                    it.value?.let { equipmentSelected ->
                        //equipments.map { equipmentDto -> equipmentDto.isSelected = equipmentDto.name == equipmentSelected.name }

                        equipmentAdapter?.submitList(
                            equipments.map { equipmentDto ->
                                if (equipmentSelected.name == equipmentDto.name){
                                    Equipment(equipmentDto.id, equipmentDto.name, equipmentDto.sport, equipmentDto.category, equipmentDto.genders, true, equipmentDto.material, equipmentDto.createdAt, equipmentDto.updatedAt)
                                }else{
                                    Equipment(equipmentDto.id, equipmentDto.name, equipmentDto.sport, equipmentDto.category, equipmentDto.genders, false, equipmentDto.material, equipmentDto.createdAt, equipmentDto.updatedAt)
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
            sport?.let { chosenSport ->
                category?.let { chosenCategory ->
                    viewModel.getEquipments(chosenSport,chosenCategory)
                }
            }
        }
    }

    private fun initRecyclers(){
        binding.include.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            equipmentAdapter = EquipmentAdapter(this@ChooseEquipmentFragment)
            adapter = equipmentAdapter
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

    override fun onItemSelected(position: Int, equipment: Equipment) {
        if (equipment.name.count()>1) viewModel.setEquipment(equipment)
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            ChooseEquipmentFragment()
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null){
            equipmentAdapter?.submitList(equipments.filter { it.name.contains(newText, true) || it.name.contains(newText, true)})
        }else{
            equipmentAdapter?.submitList(equipments)
        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        onQueryTextChange(query)
        return true
    }
}