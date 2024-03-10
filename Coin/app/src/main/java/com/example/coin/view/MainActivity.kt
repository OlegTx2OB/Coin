package com.example.coin.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.coin.R
import com.example.coin.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration


    private val mBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(
            layoutInflater
        )
    }
    private val mBottomNavView by lazy(LazyThreadSafetyMode.NONE) { mBinding.bottomNavView }
    private val mNavController by lazy(LazyThreadSafetyMode.NONE) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_frame_activity_main) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setSupportActionBar(mBinding.toolbar)

        val appBarConfiguration = AppBarConfiguration.Builder(
            setOf(
                R.id.databoard_fragment,
                R.id.add_note_fragment,
                R.id.stats_fragment,
                R.id.history_fragment
            )
        ).build()
        setupActionBarWithNavController(mNavController, appBarConfiguration)

        mBottomNavView.setOnNavigationItemSelectedListener {
            val navController = findNavController(R.id.fragment_frame_activity_main)
            navController.popBackStack(navController.graph.startDestinationId, false)
            navController.navigate(it.itemId)
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.fragment_frame_activity_main)
        when (item.itemId) {
            R.id.settings_fragment -> {
                navController.navigate(R.id.settings_fragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment_frame_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}