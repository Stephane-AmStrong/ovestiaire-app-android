package com.globodai.ovestiaire.ui.base

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.globodai.ovestiaire.data.db.UserPreferences
import com.globodai.ovestiaire.data.network.RemoteDataSource
import com.globodai.ovestiaire.data.repository.BaseRepository
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseBottomSheet<VM : BaseViewModel, B : ViewBinding, R : BaseRepository> : BottomSheetDialogFragment() {

    protected lateinit var userPreferences: UserPreferences
    protected lateinit var binding: B
    protected lateinit var viewModel: VM
    protected val remoteDataSource = RemoteDataSource()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userPreferences = UserPreferences(requireContext())
        binding = getFragmentBinding(inflater, container)
        val factory = ViewModelFactory(getFragmentRepository())
        viewModel = ViewModelProvider(requireActivity(), factory).get(getViewModel())

        lifecycleScope.launch { userPreferences.authToken.first() }
        return binding.root
    }


    /*protected fun launchImageCrop(uri: Uri, aspectRatioX : Int = 1920, aspectRatioY : Int = 1080){
        CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(aspectRatioX, aspectRatioY)
            .setCropShape(CropImageView.CropShape.RECTANGLE) // default is rectangle
            .start(requireContext(), this);
    }*/


    abstract fun getViewModel(): Class<VM>

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B

    abstract fun getFragmentRepository(): R
}