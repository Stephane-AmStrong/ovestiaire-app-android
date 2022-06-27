package com.globodai.ovestiaire.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.globodai.ovestiaire.ui.MainActivity
import com.globodai.ovestiaire.data.models.dtos.LoginRequest
import com.globodai.ovestiaire.data.network.AuthApi
import com.globodai.ovestiaire.data.network.Resource
import com.globodai.ovestiaire.data.repository.AuthRepository
import com.globodai.ovestiaire.databinding.FragmentLoginBinding
import com.globodai.ovestiaire.ui.base.BaseFragment
import com.globodai.ovestiaire.utils.enable
import com.globodai.ovestiaire.utils.handleApiError
import com.globodai.ovestiaire.utils.startNewActivity
import com.globodai.ovestiaire.utils.visible
import kotlinx.coroutines.launch

class LoginFragment  : BaseFragment<AuthViewModel, FragmentLoginBinding, AuthRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visible(false)
        binding.btnLogin.setOnClickListener {
            login()
        }

        binding.txtEmail.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login()
                return@OnEditorActionListener true
            }
            false
        })

        binding.txtPwd.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login()
                return@OnEditorActionListener true
            }
            false
        })

        viewModel.loggedIn.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveAuthData(it.value)
                        requireActivity().startNewActivity(MainActivity::class.java)

                        /* window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                         supportActionBar?.show()*/
                    }
                }

                is Resource.Failure -> handleApiError(it) { login() }
            }
        })


    }


    private fun login() {
        val email = binding.txtEmail.text.toString().trim()
        val pwd = binding.txtPwd.text.toString().trim()
        viewModel.login(LoginRequest(email, pwd))
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        AuthRepository(
            remoteDataSource.buildApi(AuthApi::class.java),
            userPreferences
        )
}