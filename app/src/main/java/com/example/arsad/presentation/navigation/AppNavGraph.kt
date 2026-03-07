package com.example.arsad.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.arsad.presentation.alerts.view.AlertsScreen
import com.example.arsad.presentation.home.view.HomeScreen
import com.example.arsad.presentation.saved.view.MapPickerScreen
import com.example.arsad.presentation.saved.view.SavedScreen
import com.example.arsad.presentation.settings.view.SettingsScreen
import com.example.arsad.presentation.splash.view.SplashScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = Modifier.fillMaxSize(),
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
    ) {
        // splash screen
        composable(Screen.Splash.route) {
            SplashScreen(onFinished = {
                navController.navigate(Screen.BottomBar.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        // Bottom Bar Screens
        composable(Screen.BottomBar.Home.route) { HomeScreen() }

        composable(Screen.BottomBar.Saved.route) {
            SavedScreen(
                snackbarHostState = snackbarHostState,
                onOpenMapPicker = { navController.navigate(Screen.MapPicker.route) }
            )
        }

        composable(Screen.BottomBar.Alerts.route) { AlertsScreen() }

        composable(Screen.BottomBar.Settings.route) { SettingsScreen() }

        // inner screens
        composable(Screen.MapPicker.route) {
            MapPickerScreen(
                onBack = { navController.popBackStack() },
                onLocationSaved = { lat, lon, name ->
                    navController.popBackStack()
                }
            )
        }
    }
}