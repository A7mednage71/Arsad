package com.example.arsad

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.arsad.data.local.ds.SettingsManager
import com.example.arsad.data.location.LocationProvider
import com.example.arsad.data.location.LocationResult
import com.example.arsad.presentation.navigation.MainScreenContainer
import com.example.arsad.ui.theme.ArsadTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Locale

class MainActivity : ComponentActivity() {

    private var canNavigateToHome = mutableStateOf(false)
    private var fetchJob: Job? = null
    private var isSplashTimerFinished = false
    private lateinit var settingsManager: SettingsManager
    private lateinit var locationProvider: LocationProvider

    // Launcher of location , notification request
    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        // move on to the Overlay step
        checkAndRequestOverlayPermission()
    }

    override fun attachBaseContext(newBase: Context) {
        val lang = runBlocking { SettingsManager(newBase).languageFlow.first() }
        val locale = Locale.forLanguageTag(lang)
        Locale.setDefault(locale)
        val config = newBase.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        settingsManager = SettingsManager(this)
        locationProvider = LocationProvider(this)

        setContent {
            val isDark by settingsManager.isDarkModeFlow.collectAsState(initial = isSystemInDarkTheme())
            ArsadTheme(darkTheme = isDark) {
                MainScreenContainer(
                    shouldNavigateNow = canNavigateToHome.value,
                    onSplashFinished = {
                        isSplashTimerFinished = true
                        startPermissionsFlow()
                    }
                )
            }
        }
    }

    // Check location request notification
    private fun startPermissionsFlow() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        val allGranted = permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

        if (allGranted) {
            checkAndRequestOverlayPermission()
        } else {
            requestPermissionsLauncher.launch(permissions.toTypedArray())
        }
    }

    // Step to allow display overlays
    private fun checkAndRequestOverlayPermission() {
        if (Settings.canDrawOverlays(this)) {
            fetchLocationAndFinalize()
        } else {
            Toast.makeText(this, getString(R.string.overlay_permission_msg), Toast.LENGTH_LONG)
                .show()
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                "package:$packageName".toUri()
            )
            startActivity(intent)
        }
    }

    private fun fetchLocationAndFinalize() {
        if (fetchJob?.isActive == true) return
        fetchJob = lifecycleScope.launch {
            val currentLang = settingsManager.languageFlow.first()
            val locationType = settingsManager.locationMethodFlow.first()
            if (locationType == "GPS") {
                Log.d("ARSAD_DEBUG", "GPS fetch")
                when (val result = locationProvider.getCurrentLocation(currentLang)) {
                    is LocationResult.Success -> {
                        settingsManager.saveLocation(result.lat, result.lon, result.name)
                    }

                    is LocationResult.Failure -> {}
                }
            }
            canNavigateToHome.value = true
        }
    }

    override fun onResume() {
        super.onResume()
        if (isSplashTimerFinished && !canNavigateToHome.value) {
            fetchLocationAndFinalize()
        }
    }
}