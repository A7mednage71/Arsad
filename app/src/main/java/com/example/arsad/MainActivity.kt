package com.example.arsad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.arsad.presentation.home.view.HomeScreen
import com.example.arsad.presentation.splash.view.SplashScreen
import com.example.arsad.ui.theme.ArsadTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArsadTheme {
                ArsadApp()
            }
        }
    }
}

@Composable
private fun ArsadApp() {
    var showSplash by rememberSaveable { mutableStateOf(true) }

    if (showSplash) {
        SplashScreen()
    } else {
        HomeScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun AppPreview() {
    ArsadTheme {
        ArsadApp()
    }
}