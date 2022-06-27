package com.globodai.ovestiaire.ui.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.globodai.ovestiaire.data.db.UserPreferences
import com.globodai.ovestiaire.data.network.RemoteDataSource
import com.globodai.ovestiaire.data.repository.BaseRepository
import com.globodai.ovestiaire.ui.AuthActivity
import com.globodai.ovestiaire.utils.startNewActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseFragment<VM : BaseViewModel, B : ViewBinding, R : BaseRepository> : Fragment() {
    private val TAG = "BaseFragment"
    protected lateinit var userPreferences: UserPreferences
    protected lateinit var binding: B
    protected lateinit var viewModel: VM
    protected val remoteDataSource = RemoteDataSource()
    lateinit var uiCommunicationListener: UICommunicationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userPreferences = UserPreferences(requireContext())
        binding = getFragmentBinding(inflater, container)
        val factory = ViewModelFactory(getFragmentRepository())
//        viewModel = ViewModelProvider(this, factory).get(getViewModel())

        //viewModel = ViewModelProvider(requireActivity(), factory).get(getViewModel())
        viewModel = ViewModelProvider(requireActivity(), factory)[getViewModel()]

        lifecycleScope.launch { userPreferences.authToken.first() }

        try{
            uiCommunicationListener = context as UICommunicationListener
        }catch(e: ClassCastException){
            Log.e(TAG, "$context must implement UICommunicationListener" )
        }

        return binding.root
    }

    fun logout() = lifecycleScope.launch{
        val authToken = userPreferences.authToken.first()
        userPreferences.clearEverything()
        requireActivity().startNewActivity(AuthActivity::class.java)
    }

    abstract fun getViewModel(): Class<VM>

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B

    abstract fun getFragmentRepository(): R
}