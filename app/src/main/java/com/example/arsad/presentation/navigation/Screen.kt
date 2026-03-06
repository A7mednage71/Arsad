package com.example.arsad.presentation.navigation

import com.example.arsad.R

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object MainContainer : Screen("main_container")
    object MapPicker : Screen("map_picker")

    sealed class BottomBar(
        val baseRoute: String,
        val title: String,
        val activeIcon: Int,
        val inactiveIcon: Int
    ) : Screen(baseRoute) {

        object Home : BottomBar(
            baseRoute = "home",
            title = "Home",
            activeIcon = R.drawable.ic_home_bold,
            inactiveIcon = R.drawable.ic_home_light
        )

        object Saved : BottomBar(
            baseRoute = "saved",
            title = "Saved",
            activeIcon = R.drawable.ic_bookmark_bold,
            inactiveIcon = R.drawable.ic_bookmark_light
        )

        object Alerts : BottomBar(
            baseRoute = "alerts",
            title = "Alerts",
            activeIcon = R.drawable.ic_notification_bold,
            inactiveIcon = R.drawable.ic_notification_light
        )

        object Settings : BottomBar(
            baseRoute = "settings",
            title = "Settings",
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