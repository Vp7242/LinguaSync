package com.linguasync.translator.feature.translator.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.linguasync.translator.R
import com.linguasync.translator.model.ListeningStatus
import com.linguasync.translator.ui.preview.LightDarkPreview
import com.linguasync.translator.ui.preview.PreviewSurface
import com.linguasync.translator.ui.theme.Spacing

// Sizes: change these to resize the button.
private val ContainerSize = 136.dp // room for the halo to grow
private val HaloSize = 96.dp
private val ButtonSize = 84.dp
private val IconSize = 40.dp

/**
 * Big round start/stop button. While listening, a halo behind it grows with the microphone level.
 *
 * @param isActive true while a session is running (the button then shows "stop").
 * @param level    microphone level 0f..1f.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MicButton(
    isActive: Boolean,
    status: ListeningStatus,
    level: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isListening = status == ListeningStatus.Listening
    val haloScale by animateFloatAsState(
        targetValue = if (isListening) 1f + level * 0.7f else 1f,
        animationSpec = tween(durationMillis = 120),
        label = "haloScale",
    )
    val pulse by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulseScale",
    )

    val containerColor = if (isActive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    val contentColor = if (isActive) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onPrimary

    Box(modifier = modifier.size(ContainerSize), contentAlignment = Alignment.Center) {
        if (isListening) {
            Box(
                modifier = Modifier
                    .size(HaloSize)
                    .scale(haloScale * pulse)
                    .background(containerColor.copy(alpha = 0.2f), CircleShape)
            )
        }
        Surface(
            onClick = onClick,
            shape = CircleShape,
            color = containerColor,
            contentColor = contentColor,
            shadowElevation = 6.dp,
            modifier = Modifier.size(ButtonSize),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = if (isActive) Icons.Rounded.Stop else Icons.Rounded.Mic,
                    contentDescription = stringResource(
                        if (isActive) R.string.stop_translation else R.string.start_translation
                    ),
                    modifier = Modifier.size(IconSize),
                )
            }
        }
    }
}

// ---- Previews ----------------------------------------------------------------------------

@LightDarkPreview
@Composable
private fun MicButtonStatesPreview() {
    PreviewSurface {
        Row(
            modifier = Modifier.padding(Spacing.Large),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small),
        ) {
            MicButton(isActive = false, status = ListeningStatus.Idle, level = 0f, onClick = {})
            MicButton(isActive = true, status = ListeningStatus.Listening, level = 0.7f, onClick = {})
            MicButton(isActive = true, status = ListeningStatus.Processing, level = 0f, onClick = {})
        }
    }
}
