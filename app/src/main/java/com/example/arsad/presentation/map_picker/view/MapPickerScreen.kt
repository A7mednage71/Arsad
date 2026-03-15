package com.example.arsad.presentation.map_picker.view

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.arsad.R
import com.example.arsad.presentation.map_picker.viewModel.MapPickerViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay

@Composable
fun MapPickerScreen(
    viewModel: MapPickerViewModel,
    onBack: () -> Unit,
    onLocationSaved: (lat: Double, lon: Double, label: String) -> Unit
) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    // Define the MapView using remember so it
    // doesn't repeat with the Recomposition

    val mapView = remember { MapView(context) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }

    // Monitor the selected location to move the camera and update the marker
    LaunchedEffect(viewModel.selectedLocation) {
        viewModel.selectedLocation?.let { geo ->
            mapView.controller.animateTo(geo)
            mapView.controller.setZoom(15.0)

            // تحديث الماركر
            mapView.overlays.filterIsInstance<Marker>().forEach { mapView.overlays.remove(it) }
            val marker = Marker(mapView).apply {
                position = geo
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                title = viewModel.locationName
            }
            mapView.overlays.add(marker)
            mapView.invalidate()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                mapView.apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    Configuration.getInstance().userAgentValue = context.packageName
                    controller.setZoom(6.0)
                    controller.setCenter(GeoPoint(26.8206, 30.8025)) // Egypt

                    val tapOverlay = object : Overlay() {
                        override fun onSingleTapConfirmed(
                            e: MotionEvent,
                            mapView: MapView
                        ): Boolean {
                            val proj =
                                mapView.projection.fromPixels(e.x.toInt(), e.y.toInt()) as GeoPoint
                            viewModel.onMapTap(proj.latitude, proj.longitude)
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
                .padding(horizontal = 12.dp, vertical = 48.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.surface.copy(alpha = 0.9f))
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }

                Spacer(modifier = Modifier.size(8.dp))

                OutlinedTextField(
                    value = viewModel.searchQuery,
                    onValueChange = { viewModel.onSearchQueryChange(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(R.string.map_search_placeholder)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = colors.primary
                        )
                    },
                    trailingIcon = {
                        if (viewModel.isLoading) CircularProgressIndicator(
                            modifier = Modifier.size(
                                20.dp
                            ), strokeWidth = 2.dp
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { viewModel.performSearch() }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colors.surface,
                        unfocusedContainerColor = colors.surface.copy(alpha = 0.9f)
                    )
                )
            }
        }

        if (viewModel.selectedLocation != null) {
            Button(
                onClick = {
                    onLocationSaved(
                        viewModel.selectedLocation!!.latitude,
                        viewModel.selectedLocation!!.longitude,
                        viewModel.locationName
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(R.string.map_save_location),
                    style = typography.titleSmall
                )
            }
        }
    }
}



