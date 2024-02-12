package com.recorder.voicerecorder.presentation.screens.player_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.recorder.voicerecorder.presentation.components.AudioItem
import com.recorder.voicerecorder.presentation.components.PlayerTopBar
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(viewModel: PlayerViewModel = koinViewModel(), onBackPressed: () -> Unit) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val state = viewModel.state.collectAsState().value

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            PlayerTopBar(
                onClick = onBackPressed,
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(9f)
                    .padding(vertical = 10.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(viewModel.state.value.audios) { audio ->
                    AudioItem(
                        audio = audio,
                        onClick = {
                            viewModel.initMediaPlayer(audio.absolutePath)
                        },
                        delete = {
                            viewModel.deleteAudio(audio.absolutePath)
                        }
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                FloatingActionButton(
                    onClick = {
                        viewModel.rewindTenSeconds()
                    },
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .defaultMinSize(minWidth = 38.dp, minHeight = 38.dp),
                ) {
                    Image(
                        imageVector = Icons.Default.Replay10,
                        contentScale = ContentScale.FillWidth,
                        contentDescription = null
                    )
                }
                FloatingActionButton(
                    modifier = Modifier.defaultMinSize(
                        minWidth = 64.dp,
                        minHeight = 64.dp
                    ),
                    onClick = {
                        if (state.isPlaying) {
                            viewModel.pauseMedia()
                        } else {
                            viewModel.playMedia()
                        }
                    }
                ) {
                    Image(
                        imageVector =
                        if (state.isPlaying)
                            Icons.Default.Pause
                        else
                            Icons.Default.PlayArrow,
                        modifier = Modifier.requiredWidth(48.dp),
                        contentScale = ContentScale.FillWidth,
                        contentDescription = null
                    )
                }
                FloatingActionButton(
                    onClick = {
                        viewModel.forwardTenSeconds()
                    },
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .defaultMinSize(minWidth = 38.dp, minHeight = 38.dp),
                ) {
                    Image(
                        imageVector = Icons.Default.Forward10,
                        contentScale = ContentScale.FillWidth,
                        contentDescription = null
                    )
                }
            }
        }
    }
}