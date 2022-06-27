package com.globodai.ovestiaire.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.globodai.ovestiaire.R
import com.globodai.ovestiaire.data.models.User
import com.globodai.ovestiaire.data.models.dtos.UserRegistrationRequest
import com.globodai.ovestiaire.data.network.AuthApi
import com.globodai.ovestiaire.data.network.Resource
import com.globodai.ovestiaire.data.network.UserApi
import com.globodai.ovestiaire.data.repository.AuthRepository
import com.globodai.ovestiaire.databinding.FragmentUserRegistrationBinding
import com.globodai.ovestiaire.ui.base.BaseFragment
import com.globodai.ovestiaire.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class UserRegistrationFragment : BaseFragment<AuthViewModel, FragmentUserRegistrationBinding, AuthRepository>(){

    lateinit var _user: User

    companion object {
        fun newInstance() = UserRegistrationFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        binding.progressBar.visible(false)

        binding.btnRegister.setOnClickListener {
            saveUser()
        }
    }


    private fun saveUser() {

        with(binding){

            if (txtName.text.toString().isEmpty()){
                txtName.error = resources.getString(R.string.error_field_required)
            }

            if (txtFirstNames.text.toString().isEmpty()){
                txtFirstNames.error = resources.getString(R.string.error_field_required)
            }

            if (txtEmail.text.toString().isEmpty()){
                txtEmail.error = resources.getString(R.string.error_field_required)
            }

            if (txtPwd.text.toString().isEmpty()){
                txtPwd.error = resources.getString(R.string.error_field_required)
            }

            if (txtConfirmPwd.text.toString().isEmpty()){
                txtConfirmPwd.error = resources.getString(R.string.error_field_required)
            }

            if (txtPwd.text.toString() != txtConfirmPwd.text.toString()){
                txtPwd.error = resources.getString(R.string.error_pwd_not_match)
                txtConfirmPwd.error = resources.getString(R.string.error_pwd_not_match)
                return
            }

            if (txtName.text.toString().isEmpty() || txtFirstNames.text.toString().isEmpty() || txtEmail.text.toString().isEmpty() || txtPwd.text.toString().isEmpty() || txtConfirmPwd.text.toString().isEmpty()) {
                return
            }
        }


        val name = binding.txtName.text.toString().trim()
        val firstName = binding.txtFirstNames.text.toString().trim()
        val email = binding.txtEmail.text.toString().trim()
        val password = binding.txtPwd.text.toString().trim()
        val confirmPassword = binding.txtConfirmPwd.text.toString().trim()

        viewModel.registerUser(UserRegistrationRequest(null,name,firstName, email, password, confirmPassword))
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUserRegistrationBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): AuthRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(AuthApi::class.java, token)

        return AuthRepository(api, userPreferences)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()

    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
    }
}