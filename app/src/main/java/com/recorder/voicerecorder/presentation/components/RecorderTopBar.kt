package com.recorder.voicerecorder.presentation.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.recorder.voicerecorder.R
import com.recorder.voicerecorder.presentation.screens.recorder_screen.RecorderState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecorderTopBar(
    state: RecorderState,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text =
                when (state) {
                    RecorderState.Paused -> state.name
                    RecorderState.Stopped -> stringResource(id = R.string.wait)
                    else -> stringResource(id = R.string.record)
                },
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        scrollBehavior = scrollBehavior
    )
}