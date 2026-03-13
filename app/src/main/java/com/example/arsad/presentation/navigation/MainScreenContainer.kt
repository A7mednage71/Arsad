package com.example.arsad.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.arsad.presentation.components.ArsadSnackbar
import com.example.arsad.ui.theme.ArsadGradient

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreenContainer() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in navItems.map { it.route }
    val isSplashScreen = currentRoute == Screen.Splash.route

    Scaffold(
        snackbarHost = {
            if (!isSplashScreen) {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    ArsadSnackbar(
                        message = data.visuals.message,
                        isError = data.visuals.actionLabel == "error",
                        onDismiss = { data.dismiss() }
                    )
                }
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ArsadGradient.screenBackground)
                .padding(
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            AppNavGraph(
                navController = navController,
                snackbarHostState = snackbarHostState
            )

            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                MainBottomNavigation(
                    navController = navController
                )
            }
        }
    }
}