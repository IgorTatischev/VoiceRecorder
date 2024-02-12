package com.recorder.voicerecorder.presentation.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.recorder.voicerecorder.R
import com.recorder.voicerecorder.services.recorder.RecorderState

@Composable
fun RecorderBottomBar(
    state: RecorderState,
    floatingClick: () -> Unit,
    actionClick: () -> Unit,
) {
    BottomAppBar(
        actions = {
            TextButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = 0.3f),
                onClick = actionClick,
                enabled = state != RecorderState.Active,
            ) {
                Text(
                    text = stringResource(id = R.string.stop),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        floatingActionButton = {
            IconButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(bottom = 10.dp),
                onClick = floatingClick
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Default.AudioFile,
                    contentDescription = "AudioPlayer",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}