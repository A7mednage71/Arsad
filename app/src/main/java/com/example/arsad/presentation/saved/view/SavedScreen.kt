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
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.example.arsad.R
import com.example.arsad.presentation.saved.view.components.SavedLocationsEmptyState
import com.example.arsad.presentation.saved.view.components.SavedLocationsList
import kotlinx.coroutines.launch

data class SavedLocation(
    val id: Int,
    val cityName: String,
    val country: String,
    val temp: String,
    val weatherIcon: ImageVector,
    val weatherStatus: String
)

@Composable
fun SavedScreen(
    modifier: Modifier = Modifier,
    onOpenMapPicker: () -> Unit = {},
    snackbarHostState: SnackbarHostState,
    navBackStackEntry: NavBackStackEntry? = null,
) {
    val scope = rememberCoroutineScope()
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    // Receive map result from MapPickerScreen
    LaunchedEffect(navBackStackEntry) {
        navBackStackEntry?.savedStateHandle?.let { handle ->
            val lat = handle.get<Double>("map_lat")
            val lon = handle.get<Double>("map_lon")
            val name = handle.get<String>("map_name")
            if (lat != null && lon != null && name != null) {
                // TODO: pass to SavedViewModel to save in DB
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
    val savedLocations = remember {
        mutableStateListOf(
            SavedLocation(1, "Sohag", "Egypt", "35°", Icons.Default.WbSunny, "Sunny"),
            SavedLocation(2, "Cairo", "Egypt", "23°", Icons.Default.WbCloudy, "Cloudy"),
            SavedLocation(3, "Alexandria", "Egypt", "20°", Icons.Default.Cloud, "Overcast"),
            SavedLocation(4, "London", "UK", "12°", Icons.Default.Cloud, "Rainy"),
            SavedLocation(5, "Dubai", "UAE", "38°", Icons.Default.WbSunny, "Clear")
        )
    }

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

            if (savedLocations.isEmpty()) {
                SavedLocationsEmptyState(modifier = Modifier.weight(1f))
            } else {
                SavedLocationsList(savedLocations, onCityClick = { })
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
