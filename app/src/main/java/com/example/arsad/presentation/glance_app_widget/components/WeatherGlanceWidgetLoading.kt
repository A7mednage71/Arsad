package com.example.arsad.presentation.glance_app_widget.components

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.example.arsad.R
import com.example.arsad.presentation.glance_app_widget.WeatherGlanceWidgetKeys
import com.example.arsad.ui.theme.WidgetPrimaryText
import com.example.arsad.util.getLocalizedContext

@SuppressLint("RestrictedApi")
@Composable
fun WeatherGlanceWidgetLoading(prefs: Preferences) {
    val context = LocalContext.current
    val selectedLang = prefs[WeatherGlanceWidgetKeys.SELECTED_LANGUAGE] ?: "en"
    val localizedContext = context.getLocalizedContext(selectedLang)

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ImageProvider(R.drawable.widget_bg_gradient))
            .appWidgetBackground()
            .cornerRadius(28.dp)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.Horizontal.CenterHorizontally) {
            Image(
                provider = ImageProvider(R.drawable.app_icon),
                contentDescription = null,
                modifier = GlanceModifier.size(48.dp)
            )

            Spacer(modifier = GlanceModifier.height(16.dp))

            Text(
                text = localizedContext.getString(R.string.widget_loading_msg),
                style = TextStyle(
                    color = WidgetPrimaryText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}