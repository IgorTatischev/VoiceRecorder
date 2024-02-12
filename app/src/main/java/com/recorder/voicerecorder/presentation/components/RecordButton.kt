package com.recorder.voicerecorder.presentation.components

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MicNone
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.recorder.voicerecorder.services.recorder.RecorderState

@Composable
fun RecordButton(
    state: RecorderState,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        modifier = Modifier
            .size(100.dp)
            .offset(y = 70.dp),
        onClick = onClick,
        shape = RoundedCornerShape(70.dp),
        containerColor = if (state == RecorderState.Active) Color.Red
        else MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {
        Icon(
            modifier = Modifier.size(40.dp),
            imageVector = when (state) {
                RecorderState.Active -> Icons.Default.Pause
                RecorderState.Paused -> Icons.Default.PlayArrow
                else -> Icons.Default.MicNone
            },
            contentDescription = null
        )
    }
}