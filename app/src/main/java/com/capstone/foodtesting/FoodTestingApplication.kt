package com.capstone.foodtesting

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.alterac.blurkit.BlurKit

@HiltAndroidApp
class FoodTestingApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        BlurKit.init(this)
    }
}