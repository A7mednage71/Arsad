package com.example.arsad.presentation.saved.view

import android.graphics.Point
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.arsad.R
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay

@Composable
fun MapPickerScreen(
    onBack: () -> Unit,
    onLocationSaved: (lat: Double, lon: Double, label: String) -> Unit
) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    var selectedLat by remember { mutableStateOf<Double?>(null) }
    var selectedLon by remember { mutableStateOf<Double?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var markerRef by remember { mutableStateOf<Marker?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {

        Configuration.getInstance().userAgentValue = context.packageName

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    controller.setZoom(6.0)
                    controller.setCenter(GeoPoint(26.8206, 30.8025)) // Egypt

                    val tapOverlay = object : Overlay() {
                        override fun onSingleTapConfirmed(
                            e: android.view.MotionEvent,
                            mapView: MapView
                        ): Boolean {
                            val point = Point(e.x.toInt(), e.y.toInt())
                            val geo = mapView.projection.fromPixels(point.x, point.y) as GeoPoint
                            selectedLat = geo.latitude
                            selectedLon = geo.longitude

                            // Remove old marker & add new one
                            markerRef?.let { mapView.overlays.remove(it) }
                            val marker = Marker(mapView).apply {
                                position = geo
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                title = ctx.getString(R.string.map_selected_location)
                            }
                            mapView.overlays.add(marker)
                            markerRef = marker
                            mapView.invalidate()
                            return true
                        }
                    }
                    overlays.add(tapOverlay)
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(horizontal = 12.dp, vertical = 48.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                // Back button
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.surface.copy(alpha = 0.9f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.map_back),
                        tint = colors.onSurface
                    )
                }

                // Search field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            stringResource(R.string.map_search_placeholder),
                            style = typography.bodyMedium,
                            color = colors.onSurface.copy(alpha = 0.5f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colors.surface.copy(alpha = 0.95f),
                        unfocusedContainerColor = colors.surface.copy(alpha = 0.9f),
                        focusedBorderColor = colors.primary,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 52.dp)
                )
            }

            if (selectedLat == null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.map_tap_hint),
                    style = typography.labelMedium,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.45f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }

        // Save button – appears only after picking
        if (selectedLat != null && selectedLon != null) {
            Button(
                onClick = {
                    onLocationSaved(
                        selectedLat!!,
                        selectedLon!!,
                        searchQuery.ifBlank { "%.4f, %.4f".format(selectedLat, selectedLon) }
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 36.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(R.string.map_save_location),
                    style = typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}



