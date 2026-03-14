package com.example.arsad.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.arsad.presentation.alerts.view.AlertsScreen
import com.example.arsad.presentation.alerts.viewModel.AlertViewModel
import com.example.arsad.presentation.home.view.HomeScreen
import com.example.arsad.presentation.home.viewModel.HomeViewModel
import com.example.arsad.presentation.map_picker.view.MapPickerScreen
import com.example.arsad.presentation.map_picker.viewModel.MapPickerViewModel
import com.example.arsad.presentation.saved.view.SavedScreen
import com.example.arsad.presentation.saved.viewModel.SavedViewModel
import com.example.arsad.presentation.settings.view.SettingsScreen
import com.example.arsad.presentation.settings.viewModel.SettingsViewModel
import com.example.arsad.presentation.splash.view.SplashScreen
import com.example.arsad.presentation.weather_details.view.WeatherDetailScreen
import com.example.arsad.presentation.weather_details.viewModel.WeatherDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    onSplashFinished: () -> Unit,
    shouldNavigateNow: Boolean,
) {

    LaunchedEffect(shouldNavigateNow) {
        if (shouldNavigateNow) {
            navController.navigate(Screen.BottomBar.Home.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = Modifier.fillMaxSize(),
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
    ) {
        // --- Splash Screen ---
        composable(Screen.Splash.route) {
            SplashScreen(onFinished = { onSplashFinished() })
        }

        // --- Home Screen ---
        composable(Screen.BottomBar.Home.route) {
            val homeViewModel: HomeViewModel = koinViewModel()
            HomeScreen(
                homeViewModel = homeViewModel,
                snackbarHostState = snackbarHostState,
                onGoToSettings = {
                    navController.navigate(Screen.BottomBar.Settings.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // --- Saved Screen ---
        composable(Screen.BottomBar.Saved.route) { backStackEntry ->
            val savedViewModel: SavedViewModel = koinViewModel()
            SavedScreen(
                viewModel = savedViewModel,
                snackbarHostState = snackbarHostState,
                onOpenMapPicker = { navController.navigate(Screen.MapPicker.route) },
                navBackStackEntry = backStackEntry,
                onLocationClicked = { id, lat, lon ->
                    navController.navigate(
                        Screen.WeatherDetails.route
                            .replace("{lat}", lat.toString())
                            .replace("{lon}", lon.toString())
                            .replace("{id}", id.toString())
                    )
                }
            )
        }

        // --- Alerts Screen ---
        composable(Screen.BottomBar.Alerts.route) {
            val alertViewModel: AlertViewModel = koinViewModel()
            AlertsScreen(viewModel = alertViewModel)
        }

        // --- Settings Screen ---
        composable(Screen.BottomBar.Settings.route) { backStackEntry ->
            val settingsViewModel: SettingsViewModel = koinViewModel()
            SettingsScreen(
                settingsViewModel = settingsViewModel,
                onOpenMapPicker = { navController.navigate(Screen.MapPicker.route) },
                navBackStackEntry = backStackEntry
            )
        }

        // --- Map Picker ---
        composable(Screen.MapPicker.route) {
            val viewModel: MapPickerViewModel = koinViewModel()
            MapPickerScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onLocationSaved = { lat, lon, name ->
                    navController.previousBackStackEntry?.savedStateHandle?.apply {
                        set("map_lat", lat)
                        set("map_lon", lon)
                        set("map_name", name)
                    }
                    navController.popBackStack()
                }
            )
        }

        // --- Weather Details ---
        composable(
            route = Screen.WeatherDetails.route,
            arguments = listOf(
                navArgument("lat") { type = NavType.FloatType },
                navArgument("lon") { type = NavType.FloatType },
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val weatherDetailViewModel: WeatherDetailViewModel = koinViewModel()

            val lat = backStackEntry.arguments?.getFloat("lat")?.toDouble() ?: 0.0
            val lon = backStackEntry.arguments?.getFloat("lon")?.toDouble() ?: 0.0
            val id = backStackEntry.arguments?.getInt("id") ?: 0

            WeatherDetailScreen(
                lat = lat,
                lon = lon,
                id = id,
                viewModel = weatherDetailViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}