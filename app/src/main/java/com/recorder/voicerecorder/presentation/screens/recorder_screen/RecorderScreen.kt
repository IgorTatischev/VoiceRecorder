package com.recorder.voicerecorder.presentation.screens.recorder_screen

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.recorder.voicerecorder.presentation.components.RecordButton
import com.recorder.voicerecorder.presentation.components.RecorderBottomBar
import com.recorder.voicerecorder.presentation.components.RecorderTopBar
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecorderScreen(viewModel: RecorderViewModel =  koinViewModel(), navigate: () -> Unit) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val state = viewModel.state.collectAsState().value.record
    val hours = viewModel.state.collectAsState().value.hours
    val minutes = viewModel.state.collectAsState().value.minutes
    val seconds = viewModel.state.collectAsState().value.seconds

    CheckPermissions()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            RecorderTopBar(
                state = state,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            RecorderBottomBar(state = state, floatingClick = navigate) {
                viewModel.onEvent(RecorderEvent.StopRecording)
            }
        },
        floatingActionButton = {
            RecordButton(state = state) {
                viewModel.onEvent(
                    event = when (state) {
                        RecorderState.Active -> RecorderEvent.PauseRecording
                        RecorderState.Paused -> RecorderEvent.ResumeRecording
                        else -> RecorderEvent.StartRecording
                    }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TimeText(state = hours)
            Spacer(modifier = Modifier.width(15.dp))
            TimeText(state = minutes)
            Spacer(modifier = Modifier.width(15.dp))
            TimeText(state = seconds)
        }
    }
}

@Composable
fun TimeText(state: String) {
    Text(
        text = state, style = TextStyle(
            fontSize = MaterialTheme.typography.displayLarge.fontSize,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun CheckPermissions() {

    val multiplePermissionsState =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            rememberMultiplePermissionsState(
                permissions = listOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.POST_NOTIFICATIONS,
                )
            )
        } else {
            rememberMultiplePermissionsState(
                permissions = listOf(
                    Manifest.permission.RECORD_AUDIO,
                )
            )
        }

    LaunchedEffect(key1 = multiplePermissionsState) {
        if (!multiplePermissionsState.allPermissionsGranted) {
            multiplePermissionsState.launchMultiplePermissionRequest()
        }
    }
}
