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
import androidx.compose.ui.unit.dp
import com.example.arsad.data.models.SavedLocationModel

@Composable
fun SavedLocationsList(
    savedLocations: List<SavedLocationModel>,
    onCityClick: (SavedLocationModel) -> Unit,
    onCityDelete: (SavedLocationModel) -> Unit
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        items(
            items = savedLocations, key = { it.id }) { location ->
            AnimatedVisibility(
                visible = true, enter = fadeIn(), exit = shrinkVertically() + fadeOut()
            ) {
                SavedCityCard(location, onDelete = {
                    onCityDelete(location)
                }, onClick = { onCityClick(location) })
            }
        }
    }
}