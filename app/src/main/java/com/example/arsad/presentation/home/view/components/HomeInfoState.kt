package com.example.arsad.presentation.home.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.arsad.R

@Composable
fun HomeInfoState(
    title: String,
    subtitle: String,
    buttonLabel: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_error_weather),
            contentDescription = null,
            modifier = Modifier.size(130.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = title,
            style = typography.headlineSmall,
            color = colors.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = subtitle,
            style = typography.bodyMedium,
            color = colors.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onButtonClick,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.onPrimary
            )
        ) {
            Text(text = buttonLabel, style = typography.labelLarge)
        }
    }
}

@Composable
fun FailureDisplay(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) = HomeInfoState(
    title = stringResource(R.string.error_title),
    subtitle = message,
    buttonLabel = stringResource(R.string.action_retry),
    onButtonClick = onRetry,
    modifier = modifier
)

@Composable
fun HomeNoLocationState(
    onGoToSettings: () -> Unit,
    modifier: Modifier = Modifier
) = HomeInfoState(
    title = stringResource(R.string.no_location_title),
    subtitle = stringResource(R.string.no_location_subtitle),
    buttonLabel = stringResource(R.string.action_go_to_settings),
    onButtonClick = onGoToSettings,
    modifier = modifier
)
