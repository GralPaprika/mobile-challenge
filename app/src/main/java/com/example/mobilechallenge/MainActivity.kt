package com.example.mobilechallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.mobilechallenge.presentation.navigation.AppNavHost
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileChallengeTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}