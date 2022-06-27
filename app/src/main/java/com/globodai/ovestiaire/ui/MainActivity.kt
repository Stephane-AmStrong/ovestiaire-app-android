package com.globodai.ovestiaire.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.globodai.ovestiaire.R
import com.globodai.ovestiaire.data.db.UserPreferences
import com.globodai.ovestiaire.databinding.ActivityMainBinding
import com.globodai.ovestiaire.ui.base.Constants.Companion.PERMISSIONS_REQUEST_READ_STORAGE
import com.globodai.ovestiaire.ui.base.UICommunicationListener
import com.globodai.ovestiaire.utils.startNewActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.*

class MainActivity : AppCompatActivity(), UICommunicationListener {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host_fragment_activity_main)

        val token = runBlocking { UserPreferences(this@MainActivity).authToken.first() }

        val expireDate = runBlocking { UserPreferences(this@MainActivity).expireDate.first() }

        if (token == null || expireDate!! <= (Date().time)) {
            startNewActivity(AuthActivity::class.java)
        } else {
            val user = runBlocking { UserPreferences(this@MainActivity).utilisateurData.first() }
//            headerBinding.txtName.text = user.name
//            headerBinding.txtEmail.text = user.email
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_sell_article, R.id.navigation_gallery, R.id.navigation_user
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.contentMain.navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun isStoragePermissionGranted(): Boolean {
        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                PERMISSIONS_REQUEST_READ_STORAGE
            )

            return false
        } else {
            // Permission has already been granted
            return true
        }
    }

/*
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
*/
}