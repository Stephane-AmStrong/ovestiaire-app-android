package com.globodai.ovestiaire.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.globodai.ovestiaire.R
import com.globodai.ovestiaire.data.db.UserPreferences
import com.globodai.ovestiaire.data.network.AuthApi
import com.globodai.ovestiaire.data.network.RemoteDataSource
import com.globodai.ovestiaire.data.network.Resource
import com.globodai.ovestiaire.data.repository.AuthRepository
import com.globodai.ovestiaire.databinding.ActivityAuthBinding
import com.globodai.ovestiaire.ui.auth.AuthViewModel
import com.globodai.ovestiaire.ui.base.ViewModelFactory
import com.globodai.ovestiaire.utils.snackbar
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class AuthActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        supportActionBar?.hide()

        binding = ActivityAuthBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val token = runBlocking { UserPreferences(this@AuthActivity).authToken.first() }
        val api = RemoteDataSource().buildApi(AuthApi::class.java, token)

        val factory = ViewModelFactory(AuthRepository(api, UserPreferences(this@AuthActivity)))

        //if(token!=null) startNewActivity(MainActivity::class.java)

        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        navController = findNavController(R.id.auth_nav_host_fragment)

/*

        viewModel.loggedIn.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveAuthData(it.value)
                        startNewActivity(MainActivity::class.java)

                       */
/* window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                        supportActionBar?.show()*//*

                    }
                }
            }
        })
*/


        viewModel.userRegistration.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.root.snackbar(resources.getString(R.string.msg_registration_success))

                    actionDependingOnTheNumberOfUsersAvailable(1)
                }

                is Resource.Failure -> {

                }
            }

        })

//        val navGraph = navController.graph;
//        navGraph.startDestination = R.id.nav_login_fragment;
//        navController.graph = navGraph




    }



    private fun actionDependingOnTheNumberOfUsersAvailable(employeesCount: Int){
        if (employeesCount<=0) {
            val navGraph = navController.graph;
            navGraph.setStartDestination(R.id.registrationFragment)
            navController.graph = navGraph
        }else{
            val navGraph = navController.graph;
            navGraph.setStartDestination(R.id.loginFragment)
            navController.graph = navGraph
        }
    }
}