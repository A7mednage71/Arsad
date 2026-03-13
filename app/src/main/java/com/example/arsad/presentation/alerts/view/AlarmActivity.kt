package com.example.arsad.presentation.alerts.view


import android.app.KeyguardManager
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.arsad.MainActivity
import com.example.arsad.data.local.ds.SettingsManager
import com.example.arsad.presentation.alerts.view.components.AlarmScreen
import com.example.arsad.ui.theme.ArsadTheme
import com.example.arsad.util.getLocalizedContext
import org.koin.android.ext.android.inject

class AlarmActivity : ComponentActivity() {
    private val settingsManager: SettingsManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        turnScreenOnAndUnlock()
        val location = intent.getStringExtra("LOCATION") ?: "Unknown"
        val description = intent.getStringExtra("DESC") ?: "Weather Warning"
        val alertId = intent.getIntExtra("ID", -1)

        setContent {
            val selectedLang by settingsManager.languageFlow.collectAsState(initial = "en")
            val localizedContext = remember(selectedLang) {
                this@AlarmActivity.getLocalizedContext(selectedLang)
            }

            CompositionLocalProvider(LocalContext provides localizedContext) {
                ArsadTheme {
                    AlarmScreen(
                        location = location,
                        description = description,
                        onDismiss = {
                            val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                            nm.cancel(alertId)
                            finish()
                        },
                        onOpenApp = {
                            val intent =
                                Intent(this@AlarmActivity, MainActivity::class.java).apply {
                                    flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                            startActivity(intent)
                            finish()
                        }
                    )
                }
            }
        }
    }

    private fun turnScreenOnAndUnlock() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            (getSystemService(KEYGUARD_SERVICE) as KeyguardManager)
                .requestDismissKeyguard(this, null)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
        }
    }
}