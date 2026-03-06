package com.example.arsad.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.arsad.presentation.home.view.HomeScreen
import com.example.arsad.ui.theme.ArsadGradient

@Composable
fun MainScreenContainer() {
    val bottomNavController = rememberNavController()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ArsadGradient.screenBackground)
    ) {
        // Content fills the entire screen (goes behind the bar)
        NavHost(
            navController = bottomNavController,
            startDestination = Screen.BottomBar.Home.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Screen.BottomBar.Home.route) { HomeScreen() }
            composable(Screen.BottomBar.Favorites.route) { PlaceholderScreen("Favorites") }
            composable(Screen.BottomBar.Alerts.route) { PlaceholderScreen("Alerts") }
            composable(Screen.BottomBar.Settings.route) { PlaceholderScreen("Settings") }
        }

        // Floating bar on top of content
        MainBottomNavigation(
            navController = bottomNavController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}