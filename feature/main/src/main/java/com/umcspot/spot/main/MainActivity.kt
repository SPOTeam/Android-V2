package com.umcspot.spot.main

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.landing.LandingViewModel
import com.umcspot.spot.landing.navigation.Landing
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val landingViewModel : LandingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )

        setContent {
            SpotTheme {
                MainScreen()
            }
        }

        intent?.data?.let { uri ->
            Log.d("DeepLink", "onCreate uri = $uri")
            landingViewModel.onSocialDeepLink(uri)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val uri = intent.data ?: return
        Log.d("DeepLink", "onNewIntent uri = $uri")
        landingViewModel.onSocialDeepLink(uri)
    }
}