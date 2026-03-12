package com.example.arsad.presentation.navigation

import com.example.arsad.R

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object MainContainer : Screen("main_container")
    object MapPicker : Screen("map_picker")
    object WeatherDetails : Screen("weather_details/{id}/{lat}/{lon}")

    sealed class BottomBar(
        val baseRoute: String,
        val titleRes: Int,
        val activeIcon: Int,
        val inactiveIcon: Int
    ) : Screen(baseRoute) {

        object Home : BottomBar(
            baseRoute = "home",
            titleRes = R.string.nav_home,
            activeIcon = R.drawable.ic_home_bold,
            inactiveIcon = R.drawable.ic_home_light
        )

        object Saved : BottomBar(
            baseRoute = "saved",
            titleRes = R.string.nav_saved,
            activeIcon = R.drawable.ic_bookmark_bold,
            inactiveIcon = R.drawable.ic_bookmark_light
        )

        object Alerts : BottomBar(
            baseRoute = "alerts",
            titleRes = R.string.nav_alerts,
            activeIcon = R.drawable.ic_notification_bold,
            inactiveIcon = R.drawable.ic_notification_light
        )

        object Settings : BottomBar(
            baseRoute = "settings",
            titleRes = R.string.nav_settings,
            activeIcon = R.drawable.ic_setting_bold,
            inactiveIcon = R.drawable.ic_setting_light
        )
    }
}

val navItems = listOf(
    Screen.BottomBar.Home,
    Screen.BottomBar.Saved,
    Screen.BottomBar.Alerts,
    Screen.BottomBar.Settings
)