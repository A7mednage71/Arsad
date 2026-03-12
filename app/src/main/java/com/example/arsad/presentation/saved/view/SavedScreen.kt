package com.example.arsad.presentation.saved.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.example.arsad.R
import com.example.arsad.data.models.Coordinates
import com.example.arsad.presentation.saved.view.components.SavedListShimmer
import com.example.arsad.presentation.saved.view.components.SavedLocationsEmptyState
import com.example.arsad.presentation.saved.view.components.SavedLocationsList
import com.example.arsad.presentation.saved.viewModel.SavedViewModel
import kotlinx.coroutines.launch


@Composable
fun SavedScreen(
    modifier: Modifier = Modifier,
    viewModel: SavedViewModel,
    onOpenMapPicker: () -> Unit = {},
    onLocationClicked: (id: Int, lat: Double, lon: Double) -> Unit,
    snackbarHostState: SnackbarHostState,
    navBackStackEntry: NavBackStackEntry? = null,
) {
    val scope = rememberCoroutineScope()
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val savedLocations by viewModel.savedLocations.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()


    // Receive map result from MapPickerScreen
    LaunchedEffect(navBackStackEntry) {
        navBackStackEntry?.savedStateHandle?.let { handle ->
            val lat = handle.get<Double>("map_lat")
            val lon = handle.get<Double>("map_lon")
            val name = handle.get<String>("map_name")
            if (lat != null && lon != null && name != null) {
                viewModel.addSavedLocation(Coordinates(lat, lon))
                scope.launch {
                    snackbarHostState.showSnackbar("Added: $name")
                }
                handle.remove<Double>("map_lat")
                handle.remove<Double>("map_lon")
                handle.remove<String>("map_name")
            }
        }
    }

    // Dummy data
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(52.dp))
            Text(
                text = stringResource(R.string.saved_title),
                style = typography.headlineMedium,
                color = colors.onBackground
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.saved_swipe_hint),
                style = typography.bodySmall,
                color = colors.onSurfaceVariant.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(20.dp))

            if (isLoading) {
                SavedListShimmer()
            } else if (savedLocations.isEmpty()) {
                SavedLocationsEmptyState(modifier = Modifier.weight(1f))
            } else {
                SavedLocationsList(
                    savedLocations, onCityClick = {
                        onLocationClicked(it.id, it.lat, it.lon)
                    },
                    onCityDelete = {
                        viewModel.deleteSavedLocation(it.id)
                    })
            }
        }

        FloatingActionButton(
            onClick = onOpenMapPicker,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 120.dp),
            containerColor = colors.primary,
            contentColor = colors.onPrimary,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.saved_add_location)
            )
        }
    }
}
