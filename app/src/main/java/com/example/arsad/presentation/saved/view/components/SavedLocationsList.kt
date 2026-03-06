package com.example.arsad.presentation.saved.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.unit.dp
import com.example.arsad.presentation.saved.view.SavedLocation

@Composable
fun SavedLocationsList(
    savedLocations: SnapshotStateList<SavedLocation>,
    onCityClick: (SavedLocation) -> Unit
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        items(
            items = savedLocations,
            key = { it.id }
        ) { location ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                SavedCityCard(
                    location,
                    onDelete = {
                        savedLocations.remove(location)
                    },
                    onClick = { onCityClick(location) }
                )
            }
        }
    }
}