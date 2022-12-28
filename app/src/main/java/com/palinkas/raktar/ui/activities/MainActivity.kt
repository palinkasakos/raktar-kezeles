package com.palinkas.raktar.ui.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.palinkas.raktar.BuildConfig
import com.palinkas.raktar.R
import com.palinkas.raktar.databinding.ActivityMainBinding
import com.palinkas.raktar.ui.common.CustomAlertDialog
import com.palinkas.raktar.utils.LoadingHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainActivityViewModel>()

    private var saveButton: MenuItem? = null
    var dialog: CustomAlertDialog? = null

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var loadingHelper: LoadingHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            loadingHelper.loadingFlow.collectLatest {
                val visibility = if (it) View.VISIBLE else View.GONE

                binding.backdrop.visibility = visibility
                binding.progressIndicator.visibility = visibility
            }
        }

        setSupportActionBar(binding.appBarMain.toolbar)

        setUpNavigation()

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_products, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
        saveButton?.isVisible = destination.id == R.id.productDetailFragment
    }

    override fun onResume() {
        super.onResume()

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.addOnDestinationChangedListener(listener)
    }

    override fun onPause() {
        super.onPause()

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.removeOnDestinationChangedListener(listener)
    }

    private fun setUpNavigation() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        setSupportActionBar(binding.appBarMain.toolbar)

        val menuItems = mutableListOf(
            R.id.nav_home
        )

        appBarConfiguration = AppBarConfiguration(
            menuItems.toSet(),
            null
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (viewModel.showConfirmationDialogBeforeBackNavigation.value == true) {
            showConfirmationDialog()

            return false
        }
return super.onSupportNavigateUp()
//        val navController = findNavController(R.id.nav_host_fragment_content_main)

//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (viewModel.showConfirmationDialogBeforeBackNavigation.value == true) {
            showConfirmationDialog()

            return
        }

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
                super.onBackPressed()
        }
    }

    private fun showConfirmationDialog() {
        dialog = CustomAlertDialog.createDialog(
            getString(R.string.back_navigation_confirmation_dialog_title),
            getString(R.string.back_navigation_confirmation_dialog_message),
            getString(R.string.yes),
            getString(R.string.cancel),
            { _, _ -> handleNavigation() },
            { _, _ -> dialog?.dismiss() }
        )

        dialog?.show(supportFragmentManager, "close_supplier_order_dialog")
    }

    private fun handleNavigation() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        navController.navigateUp()
    }

    private fun goToSettingsFragment() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        if (BuildConfig.DEBUG) {
            //navController.navigate(R.id.nav_settings)

            return
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        menu.findItem(R.id.action_settings)?.setOnMenuItemClickListener {
            goToSettingsFragment()

            true
        }

        saveButton = menu.findItem(R.id.action_save)

        saveButton?.let {
            it.setOnMenuItemClickListener {
                true
            }


            it.isVisible = navController.currentDestination?.id == R.id.productDetailFragment
        }

        return true
    }
}