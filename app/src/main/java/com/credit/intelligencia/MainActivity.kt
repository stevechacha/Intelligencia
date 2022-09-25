package com.credit.intelligencia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.credit.intelligencia.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
    }

        private fun setupBottomNavigation() {
            binding.bottomNavigation.apply {
                val navView: BottomNavigationView = findViewById(R.id.bottomNavigation)
                itemIconTintList = null

                val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
                navHostFragment.navController.apply {
                    setupWithNavController(this)
                    addOnDestinationChangedListener { _, _, args ->
                        // Top level items should have such argument with value set to true
                        isVisible = args?.getBoolean("hasBottomNav", false) == true
                    }
                    setOnItemReselectedListener { } // Do nothing when selecting same item
                }
            }
        }

}