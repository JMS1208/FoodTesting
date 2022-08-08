package com.capstone.foodtesting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.capstone.foodtesting.databinding.ActivityMainBinding
import com.capstone.foodtesting.util.hide
import com.capstone.foodtesting.util.show
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val keyHash= Utility.getKeyHash(this)
        Log.d("Hash",keyHash)
        setupBottomNavigationView()
    }

    private fun setupBottomNavigationView() {
        val host = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = host.navController

        navController.addOnDestinationChangedListener(
            object: NavController.OnDestinationChangedListener {
                override fun onDestinationChanged(
                    controller: NavController,
                    destination: NavDestination,
                    arguments: Bundle?
                ) {
                    when(destination.id) {
                        R.id.fragment_home,
                        R.id.fragment_dash_board,
                        R.id.fragment_info -> binding.bottomNavigationView.show()

                        else-> binding.bottomNavigationView.hide()
                    }
                }

            }
        )

        binding.bottomNavigationView.setupWithNavController(navController)


    }





}