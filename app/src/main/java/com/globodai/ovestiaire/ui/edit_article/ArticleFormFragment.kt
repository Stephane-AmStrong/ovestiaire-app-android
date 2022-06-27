package com.globodai.ovestiaire.ui.edit_article

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.recyclerview.widget.LinearLayoutManager
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import com.globodai.ovestiaire.R
import com.globodai.ovestiaire.data.models.Condition
import com.globodai.ovestiaire.data.models.Equipment
import com.globodai.ovestiaire.data.network.ArticleApi
import com.globodai.ovestiaire.data.network.Resource
import com.globodai.ovestiaire.data.repository.ArticleRepository
import com.globodai.ovestiaire.databinding.FragmentArticleFormBinding
import com.globodai.ovestiaire.ui.article.ArticleViewModel
import com.globodai.ovestiaire.ui.base.BaseFragment
import com.globodai.ovestiaire.ui.edit_article.adapters.TmpArticlePicturesAdapter
import com.globodai.ovestiaire.ui.edit_article.dialogs.ChooseConditionDialog
import com.globodai.ovestiaire.utils.handleApiError
import com.globodai.ovestiaire.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ArticleFormFragment :
    BaseFragment<ArticleViewModel, FragmentArticleFormBinding, ArticleRepository>(),
    TmpArticlePicturesAdapter.Interaction {

    private lateinit var uris : List<Uri>
    private var tmpArticlePicturesAdapter: TmpArticlePicturesAdapter? = null

    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage
                .activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uriContent
        }
    }
    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>

    private val TAG = "ArticleFormFragment"
    private var chosenEquipment : Equipment? = null
    private var chosenGender : String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) { uri ->
            viewModel.addTmpArticlePicture(uri)
        }

        binding.btnAddPicture.setOnClickListener {
            if (uiCommunicationListener.isStoragePermissionGranted()) {
                cropActivityResultLauncher.launch(null)
            }
        }
        binding.crdCondition.setOnClickListener(onClickListener)

        //TmpArticlePictures
        initRecyclers()
        viewModel.tmpArticlePictures.observe(viewLifecycleOwner) {
            binding.progressBar.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    uris = it.value
                    tmpArticlePicturesAdapter?.submitList(uris)
                }

                is Resource.Failure -> handleApiError(it)

                else -> {}
            }
        }

        //sportSelected
        binding.progressBar.visible(false)
        viewModel.sportSelected.observe(viewLifecycleOwner) {
            binding.progressBar.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    it.value.let { chosenSport ->
                        chosenSport?.let { chosenSportNonNull ->
                            viewModel.getBrands(chosenSportNonNull)
                        }
                    }
                }

                is Resource.Failure -> handleApiError(it)

                else -> {}
            }
        }

        //brands
        viewModel.brands.observe(viewLifecycleOwner) {
            binding.progressBar.visible(it is Resource.Loading)

            when (it) {
                is Resource.Success -> {
                    updateBrandAutoComplete(it.value)
                }

                is Resource.Failure -> handleApiError(it)

                else -> {}
            }
        }

        //materials
        viewModel.getMaterials()
        viewModel.materials.observe(viewLifecycleOwner) {
            binding.progressBar.visible(it is Resource.Loading)

            when (it) {
                is Resource.Success -> {
                    updateMaterialAutoComplete(it.value)
                }

                is Resource.Failure -> handleApiError(it)

                else -> {}
            }
        }

        //conditionSelected
        viewModel.conditionSelected.observe(viewLifecycleOwner) {
            binding.progressBar.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    it.value.let { chosenCondition ->
                        chosenCondition?.let { chosenConditionNonNull ->
                            updateCondition(chosenConditionNonNull)
                        }
                    }
                }

                is Resource.Failure -> handleApiError(it)

                else -> {}
            }
        }

        with(binding){
            txtArticleBrand.setOnItemClickListener { adapterView, view, position, id ->
                val brandName = adapterView.adapter.getItem(position) as String?
                if (brandName !=null && brandName.isNotEmpty())  viewModel.setBrand(brandName)
            }

//            txtArticleSize.setOnItemClickListener { adapterView, view, position, id ->
//                val size = adapterView.adapter.getItem(position) as String?
//                if (size !=null && size.isNotEmpty())  viewModel.setSize(size)
//            }

            txtArticleMaterial.setOnItemClickListener { adapterView, view, position, id ->
                val material = adapterView.adapter.getItem(position) as String?
                if (material !=null)  viewModel.setMaterial(material)
                txtArticleMaterial.setText(material)
            }
        }
    }

    private fun initRecyclers(){
        binding.recyclerViewPictures.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            tmpArticlePicturesAdapter = TmpArticlePicturesAdapter(this@ArticleFormFragment)
            adapter = tmpArticlePicturesAdapter
        }
    }

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.crd_condition -> {
                viewModel.getConditions()

                parentFragment?.let { parent ->
                    ChooseConditionDialog().show(
                        parent.childFragmentManager,
                        tag
                    )
                }

                true
            }

            else -> false
        }
    }

    private fun updateBrandAutoComplete(brands: List<String>) {
        val adapter =
            ArrayAdapter(requireContext(), R.layout.item_brand_auto_complete, brands)
        binding.txtArticleBrand.setAdapter(adapter)
    }

    private fun updateCondition(condition: Condition) {
        binding.txtCondition.text = condition.title
    }

//    private fun updateSizeAutoComplete(sizes: List<String>) {
//        toast(sizes.size.toString())
//        val adapter =
//            ArrayAdapter(requireContext(), R.layout.item_brand_auto_complete, sizes)
//        binding.txtArticleSize.setAdapter(adapter)
//        if (!sizes.any()) binding.txtArticleSize.enable(false)
//    }

    private fun updateMaterialAutoComplete(materials: List<String>) {
        val adapter =
            ArrayAdapter(requireContext(), R.layout.item_brand_auto_complete, materials)
        binding.txtArticleMaterial.setAdapter(adapter)
    }

    override fun getViewModel() = ArticleViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentArticleFormBinding.inflate(inflater, container, false)


    override fun getFragmentRepository(): ArticleRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ArticleApi::class.java, token)
        return ArticleRepository(api)
    }

    companion object {
        @JvmStatic
        fun newInstance() =ArticleFormFragment()
    }

    override fun onItemSelected(position: Int, uri: Uri) {
        viewModel.removeTmpArticlePicture(uri)
    }
}