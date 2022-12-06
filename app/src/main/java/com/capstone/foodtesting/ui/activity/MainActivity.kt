package com.capstone.foodtesting.ui.activity


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.WindowCompat
import androidx.core.view.setPadding
import androidx.databinding.adapters.ViewBindingAdapter.setPadding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.capstone.foodtesting.databinding.ActivityMainBinding
import com.capstone.foodtesting.util.Constants
import com.capstone.foodtesting.util.navigationHeight
import com.capstone.foodtesting.util.statusBarHeight
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationRequest.create
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.security.MessageDigest
import com.capstone.foodtesting.R

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var navController: NavController

    private var dispatchAvailable: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        setupInitFragment()


    }

    private fun setupInitFragment() {
        val host = supportFragmentManager.findFragmentById(R.id.navHostFragment)  as NavHostFragment
        navController = host.navController


    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        // focus 를 받는 view 영역이 아닌 곳을 터치했을 때 소프트 키보드를 내림
        ev?.apply {
            if(dispatchAvailable) {
                currentFocus?.apply {
                    val rect = Rect()
                    getGlobalVisibleRect(rect)
                    val x: Int = ev.x.toInt()
                    val y: Int = ev.y.toInt()
                    if(!rect.contains(x,y)) {
                        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                        inputMethodManager?.apply {
                            hideSoftInputFromWindow(currentFocus!!.windowToken,0)
                            currentFocus!!.clearFocus()
                        }
                    }

                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    fun setDispatchTouchAvailable(isAvailable: Boolean) {
        dispatchAvailable = isAvailable
    }

}