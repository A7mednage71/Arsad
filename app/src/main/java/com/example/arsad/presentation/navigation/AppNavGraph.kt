package com.example.arsad.presentation.navigation

import android.app.Application
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.arsad.data.local.datasource.WeatherLocalDataSourceImpl
import com.example.arsad.data.local.db.WeatherDatabase
import com.example.arsad.data.remote.datasource.WeatherRemoteDataSourceImpl
import com.example.arsad.data.remote.network.RetrofitHelper
import com.example.arsad.data.repository.WeatherRepositoryImpl
import com.example.arsad.presentation.alerts.view.AlertsScreen
import com.example.arsad.presentation.alerts.viewModel.AlertViewModel
import com.example.arsad.presentation.home.view.HomeScreen
import com.example.arsad.presentation.home.viewModel.HomeViewModel
import com.example.arsad.presentation.home.viewModel.HomeViewModelFactory
import com.example.arsad.presentation.map_picker.view.MapPickerScreen
import com.example.arsad.presentation.saved.view.SavedScreen
import com.example.arsad.presentation.saved.viewModel.SavedViewModel
import com.example.arsad.presentation.saved.viewModel.SavedViewModelFactory
import com.example.arsad.presentation.settings.view.SettingsScreen
import com.example.arsad.presentation.settings.viewModel.SettingsViewModel
import com.example.arsad.presentation.settings.viewModel.SettingsViewModelFactory
import com.example.arsad.presentation.splash.view.SplashScreen
import com.example.arsad.presentation.weather_details.view.WeatherDetailScreen
import com.example.arsad.presentation.weather_details.viewModel.WeatherDetailViewModel
import com.example.arsad.presentation.weather_details.viewModel.WeatherDetailViewModelFactory
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    onSplashFinished: () -> Unit,
    shouldNavigateNow: Boolean,
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val repository = remember {
        val weatherDao = WeatherDatabase.getInstance(context).weatherDao()
        val savedLocationDao = WeatherDatabase.getInstance(context).savedLocationDao()
        val weatherAlertDao = WeatherDatabase.getInstance(context).weatherAlertDao()
        val localDataSource =
            WeatherLocalDataSourceImpl(weatherDao, savedLocationDao, weatherAlertDao)
        val remoteDataSource = WeatherRemoteDataSourceImpl(RetrofitHelper.service)
        WeatherRepositoryImpl(remoteDataSource, localDataSource)
    }

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
        // splash screen
        composable(Screen.Splash.route) {
            SplashScreen(onFinished = {
                onSplashFinished()
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
                snackbarHostState = snackbarHostState,
                onGoToSettings = {
                    navController.navigate(Screen.BottomBar.Settings.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.BottomBar.Saved.route) { backStackEntry ->
            val viewModel: SavedViewModel = viewModel(
                factory = SavedViewModelFactory(application, repository)
            )
            SavedScreen(
                snackbarHostState = snackbarHostState,
                onOpenMapPicker = { navController.navigate(Screen.MapPicker.route) },
                navBackStackEntry = backStackEntry,
                viewModel = viewModel,
                onLocationClicked = { id, lat, lon ->
                    navController.navigate(
                        Screen.WeatherDetails.route
                            .replace("{lat}", lat.toFloat().toString())
                            .replace("{lon}", lon.toFloat().toString())
                            .replace("{id}", id.toString())
                    )
                }
            )
        }

        composable(Screen.BottomBar.Alerts.route) {
            val alertViewModel: AlertViewModel = koinViewModel()
            AlertsScreen(
                viewModel = alertViewModel
            )
        }

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

        composable(
            route = Screen.WeatherDetails.route,
            arguments = listOf(
                navArgument("lat") { type = NavType.FloatType },
                navArgument("lon") { type = NavType.FloatType },
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->

            val weatherDetailViewModel: WeatherDetailViewModel = viewModel(
                factory = WeatherDetailViewModelFactory(repository, application)
            )

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