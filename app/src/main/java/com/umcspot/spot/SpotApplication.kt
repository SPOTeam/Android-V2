package com.umcspot.spot

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SpotApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}