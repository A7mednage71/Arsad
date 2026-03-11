package com.example.arsad.presentation.navigation

import android.app.Application
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.arsad.data.local.datasource.WeatherLocalDataSourceImpl
import com.example.arsad.data.local.db.WeatherDatabase
import com.example.arsad.data.remote.datasource.WeatherRemoteDataSourceImpl
import com.example.arsad.data.remote.network.RetrofitHelper
import com.example.arsad.data.repository.WeatherRepositoryImpl
import com.example.arsad.presentation.alerts.view.AlertsScreen
import com.example.arsad.presentation.home.view.HomeScreen
import com.example.arsad.presentation.home.viewModel.HomeViewModel
import com.example.arsad.presentation.home.viewModel.HomeViewModelFactory
import com.example.arsad.presentation.map_picker.view.MapPickerScreen
import com.example.arsad.presentation.saved.view.SavedScreen
import com.example.arsad.presentation.settings.view.SettingsScreen
import com.example.arsad.presentation.settings.viewModel.SettingsViewModel
import com.example.arsad.presentation.settings.viewModel.SettingsViewModelFactory
import com.example.arsad.presentation.splash.view.SplashScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val repository = remember {
        val dao = WeatherDatabase.getInstance(context).weatherDao()
        val localDataSource = WeatherLocalDataSourceImpl(dao)
        val remoteDataSource = WeatherRemoteDataSourceImpl(RetrofitHelper.service)
        WeatherRepositoryImpl(remoteDataSource, localDataSource)
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
        // splash screen
        composable(Screen.Splash.route) {
            SplashScreen(onFinished = {
                navController.navigate(Screen.BottomBar.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        // Bottom Bar Screens
        composable(Screen.BottomBar.Home.route) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(
                    repository = repository,
                    application = application
                )
            )
            HomeScreen(
                homeViewModel = homeViewModel,
                snackbarHostState = snackbarHostState
            )
        }

        composable(Screen.BottomBar.Saved.route) { backStackEntry ->
            SavedScreen(
                snackbarHostState = snackbarHostState,
                onOpenMapPicker = { navController.navigate(Screen.MapPicker.route) },
                navBackStackEntry = backStackEntry
            )
        }

        composable(Screen.BottomBar.Alerts.route) { AlertsScreen() }

        composable(Screen.BottomBar.Settings.route) { backStackEntry ->
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(application)
            )
            SettingsScreen(
                settingsViewModel = settingsViewModel,
                onOpenMapPicker = { navController.navigate(Screen.MapPicker.route) },
                navBackStackEntry = backStackEntry
            )
        }

        // inner screens
        composable(Screen.MapPicker.route) {
            MapPickerScreen(
                onBack = { navController.popBackStack() },
                onLocationSaved = { lat, lon, name ->
                    // Write result into the caller's savedStateHandle
                    navController.previousBackStackEntry?.savedStateHandle?.apply {
                        set("map_lat", lat)
                        set("map_lon", lon)
                        set("map_name", name)
                    }
                    navController.popBackStack()
                }
            )
        }
    }
}